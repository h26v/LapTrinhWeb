package vn.iotstar.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.SessionUtil_24162046;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Bat buoc dang nhap cho gio hang / thanh toan / don hang. Khai bao trong web.xml (truoc Sitemesh).
 * Neu la GET thi nho lai trang dang mo (?next=) de dang nhap xong quay ve.
 */
public class AuthFilter_24162046 extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (SessionUtil_24162046.currentUser(req) != null) {
            chain.doFilter(req, resp);
            return;
        }

        String loginUrl = req.getContextPath() + "/login";
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            String path = req.getRequestURI().substring(req.getContextPath().length());
            if (req.getQueryString() != null) {
                path += "?" + req.getQueryString();
            }
            loginUrl += "?next=" + URLEncoder.encode(path, StandardCharsets.UTF_8);
        }
        resp.sendRedirect(loginUrl);
    }
}
