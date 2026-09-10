package vn.iotstar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.ImageUploadUtil;

/**
 * Phuc vu anh da upload qua URL {@code /image?fname=category/abc.jpg}.
 *
 * <p>Chong path traversal: moi duong dan deu duoc chuan hoa va phai nam trong
 * thu muc upload truoc khi doc.
 */
@Controller
public class DownloadImageController {

    private final ImageUploadUtil imageUploadUtil;

    public DownloadImageController(ImageUploadUtil imageUploadUtil) {
        this.imageUploadUtil = imageUploadUtil;
    }

    @GetMapping("/image")
    public void image(@RequestParam(value = "fname", required = false) String fname,
            HttpServletResponse response) throws IOException {

        if (fname == null || fname.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path root = imageUploadUtil.getRoot();
        Path target = root.resolve(fname).normalize();
        // Chan path traversal: fname dang ../../ se tro ra ngoai thu muc upload.
        if (!target.startsWith(root) || !Files.isRegularFile(target)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = imageMimeType(target.getFileName().toString());
        if (mime == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(mime);
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentLengthLong(Files.size(target));
        response.setHeader("Cache-Control", "private, max-age=3600");

        try (InputStream in = Files.newInputStream(target);
                OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }

    private String imageMimeType(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".bmp")) return "image/bmp";
        return null;
    }
}
