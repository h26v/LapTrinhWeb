package vn.iotstar.controller.cookie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

@WebServlet(urlPatterns = { "/cookie/login" })
public class LoginCookieController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/cookie/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = ValidationUtil.trim(req.getParameter("username"));
        String password = req.getParameter("password");

        String error = validateLogin(username, password);
        if (error != null) {
            forwardLogin(req, resp, username, error);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            Cookie cookie = new Cookie(Constant.COOKIE_USERNAME, username);
            cookie.setMaxAge(30 * 60); // 30 phut
            cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath() + "/cookie/hello");
        } else {
            forwardLogin(req, resp, username, "Tai khoan hoac mat khau khong dung");
        }
    }

    private String validateLogin(String username, String password) {
        if (ValidationUtil.isBlank(username)) {
            return "Vui long nhap ten dang nhap.";
        }
        if (username.length() > 100) {
            return "Ten dang nhap khong duoc vuot qua 100 ky tu.";
        }
        if (ValidationUtil.isBlank(password)) {
            return "Vui long nhap mat khau.";
        }
        return null;
    }

    private void forwardLogin(HttpServletRequest req, HttpServletResponse resp, String username,
            String alert) throws ServletException, IOException {
        req.setAttribute("username", username);
        req.setAttribute("alert", alert);
        req.getRequestDispatcher("/views/cookie/login.jsp").forward(req, resp);
    }
}
