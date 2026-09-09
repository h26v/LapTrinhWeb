package vn.iotstar.controller.session;

import java.io.IOException;
import java.nio.file.Path;

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
import vn.iotstar.util.ImageUploadUtil;
import vn.iotstar.util.ImageUploadUtil.SavedFile;
import vn.iotstar.util.ImageUploadUtil.UploadValidationException;
import vn.iotstar.util.ValidationUtil;

@WebServlet(urlPatterns = { "/session/profile", "/session/profile/update" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class ProfileSessionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PROFILE_DIR = "profile";
    private static final String IMAGE_FIELD = "images";
    private static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;

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
        String fullName = ValidationUtil.trim(req.getParameter("fullname"));
        if (fullName == null) {
            // Giu tuong thich voi cac form cu dung camelCase.
            fullName = ValidationUtil.trim(req.getParameter("fullName"));
        }
        String phone = ValidationUtil.trim(req.getParameter("phone"));

        String validationError = validate(fullName, phone);
        if (validationError != null) {
            forwardWithError(req, resp, account, fullName, phone, validationError);
            return;
        }

        SavedFile upload = null;
        try {
            Part image = req.getPart(IMAGE_FIELD);
            upload = ImageUploadUtil.saveImage(image, PROFILE_DIR, MAX_IMAGE_BYTES);

            String newAvatar = upload == null ? null : upload.relativePath();
            String oldAvatar = account.getAvatar();
            User updated = userService.updateProfile(account.getId(), fullName, phone, newAvatar);
            session.setAttribute(Constant.SESSION_ACCOUNT, updated);
            session.setAttribute("profileSuccess", "Cap nhat thong tin ca nhan thanh cong.");

            // Chi xoa file cu sau khi giao dich JPA da commit thanh cong.
            if (newAvatar != null) {
                deleteLocalFile(oldAvatar);
            }
            resp.sendRedirect(req.getContextPath() + "/session/profile");
        } catch (UploadValidationException e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            forwardWithError(req, resp, account, fullName, phone, e.getMessage());
        } catch (IllegalStateException e) {
            deleteIfUploaded(upload == null ? null : upload.file());
            forwardWithError(req, resp, account, fullName, phone,
                    "File upload vuot qua gioi han cho phep.");
        } catch (Exception e) {
            deleteIfUploaded(upload == null ? null : upload.file());
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
        try {
            ValidationUtil.requireLength(fullName, "Ho va ten", 1, 255);
            ValidationUtil.requirePhone(phone);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private void deleteLocalFile(String relativePath) {
        try {
            ImageUploadUtil.deleteLocalFile(relativePath);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file upload cu", e);
        }
    }

    private void deleteIfUploaded(Path file) {
        try {
            ImageUploadUtil.deleteIfExists(file);
        } catch (IOException e) {
            getServletContext().log("Khong the xoa file upload moi", e);
        }
    }
}
