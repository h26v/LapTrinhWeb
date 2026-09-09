package vn.iotstar.util;

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
import javax.servlet.http.Part;

public final class ImageUploadUtil {
    private static final int MAX_IMAGE_WIDTH = 4096;
    private static final int MAX_IMAGE_HEIGHT = 4096;
    private static final long MAX_IMAGE_PIXELS = 16L * 1024 * 1024;

    private ImageUploadUtil() {
    }

    public static SavedFile saveImage(Part part, String subDir, long maxBytes)
            throws IOException, UploadValidationException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > maxBytes) {
            throw new UploadValidationException("Anh khong duoc vuot qua " + (maxBytes / 1024 / 1024) + " MB.");
        }
        ImageMetadata metadata = inspectImage(part);
        File dir = new File(Constant.DIR, subDir);
        Files.createDirectories(dir.toPath());

        Path root = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = root.resolve(subDir).resolve(UUID.randomUUID() + "." + metadata.extension())
                .normalize();
        if (!target.startsWith(root)) {
            throw new IOException("Duong dan upload khong hop le");
        }

        Path temporary = Files.createTempFile(dir.toPath(), ".upload-", ".tmp");
        try {
            try (InputStream input = part.getInputStream()) {
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

    public static void deleteLocalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank() || relativePath.startsWith("https")) {
            return;
        }
        Path root = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = root.resolve(relativePath).normalize();
        if (target.startsWith(root)) {
            Files.deleteIfExists(target);
        }
    }

    public static void deleteIfExists(Path path) throws IOException {
        if (path != null) {
            Files.deleteIfExists(path);
        }
    }

    private static ImageMetadata inspectImage(Part part) throws IOException, UploadValidationException {
        try (InputStream input = part.getInputStream();
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

    private static String extensionForFormat(String format) {
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
