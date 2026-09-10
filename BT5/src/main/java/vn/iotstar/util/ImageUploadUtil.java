package vn.iotstar.util;

import java.awt.image.BufferedImage;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Luu anh upload bang MultipartFile cua Spring.
 *
 * <p>Quy tac an toan giu tu BT4: khong dung ten file client gui len, doi ten bang UUID,
 * kiem tra noi dung thuc su la anh bang {@link ImageIO}, gioi han kich thuoc,
 * va chan path traversal khi doc/xoa file.
 */
@Component
public class ImageUploadUtil {
    private static final int MAX_IMAGE_WIDTH = 4096;
    private static final int MAX_IMAGE_HEIGHT = 4096;
    private static final long MAX_IMAGE_PIXELS = 16L * 1024 * 1024;
    private static final long DEFAULT_MAX_BYTES = 5L * 1024 * 1024;

    /** Thu muc goc chua anh, cau hinh qua bt5.upload.dir. */
    private final Path root;

    public ImageUploadUtil(@Value("${bt5.upload.dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public Path getRoot() {
        return root;
    }

    /**
     * Luu anh vao thu muc con {@code subDir}, tra ve duong dan tuong doi
     * (vi du {@code category/abc.jpg}) de luu vao database.
     *
     * @return null neu khong co file nao duoc gui len.
     */
    public SavedFile saveImage(MultipartFile file, String subDir) throws IOException, UploadValidationException {
        return saveImage(file, subDir, DEFAULT_MAX_BYTES);
    }

    public SavedFile saveImage(MultipartFile file, String subDir, long maxBytes)
            throws IOException, UploadValidationException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getSize() > maxBytes) {
            throw new UploadValidationException("Anh khong duoc vuot qua " + (maxBytes / 1024 / 1024) + " MB.");
        }

        ImageMetadata metadata = inspectImage(file);
        Path dir = root.resolve(subDir).normalize();
        Files.createDirectories(dir);

        Path target = dir.resolve(UUID.randomUUID() + "." + metadata.extension()).normalize();
        if (!target.startsWith(root)) {
            throw new IOException("Duong dan upload khong hop le");
        }

        Path temporary = Files.createTempFile(dir, ".upload-", ".tmp");
        try {
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, temporary, StandardCopyOption.REPLACE_EXISTING);
            }
            if (Files.size(temporary) > maxBytes) {
                throw new UploadValidationException("Anh khong duoc vuot qua " + (maxBytes / 1024 / 1024) + " MB.");
            }
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(temporary, target);
            }
            return new SavedFile(target, subDir + "/" + target.getFileName());
        } catch (Exception e) {
            Files.deleteIfExists(temporary);
            throw e;
        }
    }

    /**
     * Xoa file theo duong dan tuong doi. Bo qua URL http(s) va chan path traversal.
     */
    public void deleteLocalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()
                || relativePath.startsWith("https://") || relativePath.startsWith("http://")) {
            return;
        }
        Path target = root.resolve(relativePath).normalize();
        if (target.startsWith(root)) {
            Files.deleteIfExists(target);
        }
    }

    public void deleteIfExists(Path path) throws IOException {
        if (path != null) {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Kiem tra file gui len co phai anh hop le khong bang cach doc header thuc su,
     * khong tin phan mo rong ten file.
     */
    private ImageMetadata inspectImage(MultipartFile file) throws IOException, UploadValidationException {
        try (InputStream input = file.getInputStream();
                ImageInputStream imageInput = ImageIO.createImageInputStream(input)) {
            if (imageInput == null) {
                throw new UploadValidationException("File upload khong phai anh hop le.");
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInput);
            if (!readers.hasNext()) {
                throw new UploadValidationException("Chi chap nhan anh PNG, JPEG, GIF hoac BMP.");
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInput, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || width > MAX_IMAGE_WIDTH
                        || height > MAX_IMAGE_HEIGHT || (long) width * height > MAX_IMAGE_PIXELS) {
                    throw new UploadValidationException("Kich thuoc anh khong duoc ho tro.");
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null) {
                    throw new UploadValidationException("File upload khong phai anh hop le.");
                }
                String extension = extensionForFormat(reader.getFormatName().toLowerCase(Locale.ROOT));
                if (extension == null) {
                    throw new UploadValidationException("Chi chap nhan anh PNG, JPEG, GIF hoac BMP.");
                }
                return new ImageMetadata(extension);
            } catch (UploadValidationException e) {
                throw e;
            } catch (Exception e) {
                throw new UploadValidationException("File upload khong phai anh hop le.");
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

    private record ImageMetadata(String extension) {
    }

    public record SavedFile(Path file, String relativePath) {
    }

    public static class UploadValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public UploadValidationException(String message) {
            super(message);
        }
    }
}
