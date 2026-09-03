package vn.iotstar.controller.category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/image" })
public class DownloadImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fname = req.getParameter("fname");
        if (fname == null || fname.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path root = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = root.resolve(fname).normalize();
        // Chan path traversal: fname dang ../../ se tro ra ngoai thu muc upload
        if (!target.startsWith(root) || !Files.isRegularFile(target)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = target.toFile();
        String mime = imageMimeType(file.getName());
        if (mime == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(mime);
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setContentLengthLong(file.length());
        resp.setHeader("Cache-Control", "private, max-age=3600");

        try (InputStream in = Files.newInputStream(target);
                OutputStream out = resp.getOutputStream()) {
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
