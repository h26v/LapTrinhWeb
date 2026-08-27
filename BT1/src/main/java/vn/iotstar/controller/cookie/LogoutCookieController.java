package vn.iotstar.controller.cookie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/cookie/logout" })
public class LogoutCookieController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(Constant.COOKIE_USERNAME)) {
                    c.setMaxAge(0); // xoa cookie
                    c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                    resp.addCookie(c);
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/cookie/login");
    }
}
