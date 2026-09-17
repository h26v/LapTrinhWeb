package vn.iotstar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalName);
        String storedName = UUID.randomUUID() + (extension == null ? "" : "." + extension.toLowerCase());
        try {
            Files.createDirectories(uploadRoot);
            Path destination = uploadRoot.resolve(storedName).normalize();
            if (!destination.getParent().equals(uploadRoot)) {
                throw new IllegalArgumentException("Tên file không hợp lệ");
            }
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/" + storedName;
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể lưu file upload", exception);
        }
    }

    public String resourceLocation() {
        String location = uploadRoot.toUri().toString();
        return location.endsWith("/") ? location : location + "/";
    }
}
