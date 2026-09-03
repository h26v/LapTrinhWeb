package vn.iotstar.controller.cookie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/cookie/hello" })
public class HelloCookieController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(Constant.COOKIE_USERNAME)) {
                    name = c.getValue();
                }
            }
        }
        if (name.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cookie/login");
            return;
        }
        req.setAttribute("username", name);
        req.getRequestDispatcher("/views/cookie/hello.jsp").forward(req, resp);
    }
}
