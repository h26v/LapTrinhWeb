package vn.iotstar.controller.session;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/session/profile", "/session/profile/update" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class ProfileSessionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PROFILE_DIR = "profile";
    private static final String IMAGE_FIELD = "images";
    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;
    private static final int MAX_IMAGE_WIDTH = 4096;
    private static final int MAX_IMAGE_HEIGHT = 4096;
    private static final long MAX_IMAGE_PIXELS = 16L * 1024 * 1024;

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User account = getSessionAccount(session);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/session/login");
            return;
        }

        // Doc lai tu DB de profile luon hien thi du lieu moi nhat.
        User fresh = userService.findById(account.getId());
        if (fresh == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/session/login");
            return;
        }
        session.setAttribute(Constant.SESSION_ACCOUNT, fresh);
        req.setAttribute("account", fresh);
        req.getRequestDispatcher("/views/session/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User account = getSessionAccount(session);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/session/login");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String fullName = trim(req.getParameter("fullname"));
        if (fullName == null) {
            // Giữ tương thích với các form cũ dùng camelCase.
            fullName = trim(req.getParameter("fullName"));
        }
        String phone = trim(req.getParameter("phone"));
        String validationError = validate(fullName, phone);
        if (validationError != null) {
            forwardWithError(req, resp, account, fullName, phone, validationError);
            return;
        }

        Path newFile = null;
        String newAvatar = null;
        try {
            Part image = req.getPart(IMAGE_FIELD);
            if (image != null && image.getSize() > 0) {
                if (image.getSize() > MAX_IMAGE_BYTES) {
                    throw new ProfileValidationException("Anh khong duoc vuot qua 5 MB.");
                }
                ImageUpload upload = saveImage(image);
                newFile = upload.file();
                newAvatar = upload.relativePath();
            }

            String oldAvatar = account.getAvatar();
            User updated = userService.updateProfile(account.getId(), fullName, phone, newAvatar);
            session.setAttribute(Constant.SESSION_ACCOUNT, updated);
            session.setAttribute("profileSuccess", "Cap nhat thong tin ca nhan thanh cong.");

            // Chi xoa file cu sau khi giao dich JPA da commit thanh cong.
            if (newAvatar != null) {
                deleteLocalFile(oldAvatar);
            }
            resp.sendRedirect(req.getContextPath() + "/session/profile");
        } catch (ProfileValidationException e) {
            deleteIfExists(newFile);
            forwardWithError(req, resp, account, fullName, phone, e.getMessage());
        } catch (IllegalStateException e) {
            deleteIfExists(newFile);
            forwardWithError(req, resp, account, fullName, phone,
                    "File upload vuot qua gioi han cho phep.");
        } catch (Exception e) {
            deleteIfExists(newFile);
            getServletContext().log("Khong the cap nhat profile", e);
            forwardWithError(req, resp, account, fullName, phone,
                    "Cap nhat that bai. Vui long thu lai.");
        }
    }

    private User getSessionAccount(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(Constant.SESSION_ACCOUNT);
        return value instanceof User ? (User) value : null;
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, User account,
            String fullName, String phone, String error) throws ServletException, IOException {
        req.setAttribute("account", account);
        req.setAttribute("formFullName", fullName);
        req.setAttribute("formPhone", phone);
        req.setAttribute("profileError", error);
        req.getRequestDispatcher("/views/session/profile.jsp").forward(req, resp);
    }

    private String validate(String fullName, String phone) {
        if (fullName == null || fullName.isEmpty() || fullName.length() > 255) {
            return "Ho va ten phai co tu 1 den 255 ky tu.";
        }
        if (phone != null && !phone.isEmpty()
                && !phone.matches("[0-9+() .-]{7,20}")) {
            return "So dien thoai khong hop le.";
        }
        return null;
    }

    private ImageUpload saveImage(Part part) throws IOException, ProfileValidationException {
        ImageMetadata metadata = inspectImage(part);
        File dir = new File(Constant.DIR, PROFILE_DIR);
        Files.createDirectories(dir.toPath());

        Path root = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = root.resolve(PROFILE_DIR).resolve(UUID.randomUUID() + "." + metadata.extension())
                .normalize();
        if (!target.startsWith(root)) {
            throw new IOException("Duong dan upload khong hop le");
        }

        Path temporary = Files.createTempFile(dir.toPath(), ".profile-", ".tmp");
        try {
            try (InputStream input = part.getInputStream()) {
                Files.copy(input, temporary, StandardCopyOption.REPLACE_EXISTING);
            }
            if (Files.size(temporary) > MAX_IMAGE_BYTES) {
                throw new ProfileValidationException("Anh khong duoc vuot qua 5 MB.");
            }
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(temporary, target);
            }
            return new ImageUpload(target, PROFILE_DIR + "/" + target.getFileName());
        } catch (Exception e) {
            Files.deleteIfExists(temporary);
            throw e;
        }
    }

    /**
     * Detects the actual image format and decodes the first frame. The client filename and MIME
     * type are deliberately not used to decide what is stored.
     */
    private ImageMetadata inspectImage(Part part) throws IOException, ProfileValidationException {
        try (InputStream input = part.getInputStream();
                ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {
            if (imageInput == null) {
                throw new ProfileValidationException("File upload khong phai anh hop le.");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new ProfileValidationException("Chi chap nhan anh PNG, JPEG, GIF hoac BMP.");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || width > MAX_IMAGE_WIDTH
                        || height > MAX_IMAGE_HEIGHT || (long) width * height > MAX_IMAGE_PIXELS) {
                    throw new ProfileValidationException("Kich thuoc anh khong duoc ho tro.");
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null) {
                    throw new ProfileValidationException("File upload khong phai anh hop le.");
                }
                String format = reader.getFormatName().toLowerCase(Locale.ROOT);
                String extension = extensionForFormat(format);
                if (extension == null) {
                    throw new ProfileValidationException("Chi chap nhan anh PNG, JPEG, GIF hoac BMP.");
                }
                return new ImageMetadata(extension);
            } catch (ProfileValidationException e) {
                throw e;
            } catch (Exception e) {
                throw new ProfileValidationException("File upload khong phai anh hop le.");
            } finally {
                reader.dispose();
            }
        }
    }

    private String extensionForFormat(String format) {
        return switch (format) {
        case "png" -> "png";
        case "jpeg", "jpg" -> "jpg";
        case "gif" -> "gif";
        case "bmp" -> "bmp";
        default -> null;
        };
    }

    private void deleteLocalFile(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        Path root = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = root.resolve(relativePath).normalize();
        if (target.startsWith(root)) {
            deleteIfExists(target);
        }
    }

    private void deleteIfExists(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file upload cu", e);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private record ImageMetadata(String extension) {
    }

    private record ImageUpload(Path file, String relativePath) {
    }

    private static class ProfileValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        ProfileValidationException(String message) {
            super(message);
        }
    }
}
