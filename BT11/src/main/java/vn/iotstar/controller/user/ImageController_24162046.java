package vn.iotstar.controller.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.Constant_24162046;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tra ve anh bia da upload (luu trong Constant.UPLOAD_DIR).
 */
@WebServlet(urlPatterns = "/image")
public class ImageController_24162046 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fname = req.getParameter("fname");
        if (fname == null || fname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path dir = Paths.get(Constant_24162046.UPLOAD_DIR).toAbsolutePath().normalize();
        Path file = dir.resolve(fname).normalize();
        if (!file.startsWith(dir) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String type = getServletContext().getMimeType(file.getFileName().toString());
        resp.setContentType(type != null ? type : "application/octet-stream");
        resp.setContentLengthLong(Files.size(file));
        Files.copy(file, resp.getOutputStream());
    }
}
