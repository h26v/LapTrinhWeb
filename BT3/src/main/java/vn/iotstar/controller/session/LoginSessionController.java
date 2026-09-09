package vn.iotstar.controller.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.PasswordUtil;

@WebServlet(urlPatterns = { "/session/login" })
public class LoginSessionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/session/profile");
            return;
        }
        req.getRequestDispatcher("/views/session/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        User found = userService.findByUsername(username);
        if (found != null && found.getActive() != 1
                && PasswordUtil.matches(password, found.getPassWord())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("activationUserId", found.getId());
            session.setAttribute("activationEmail", found.getEmail());
            req.setAttribute("alert", "Tai khoan chua kich hoat. Vui long nhap OTP trong email.");
            req.getRequestDispatcher("/views/session/register-verify.jsp").forward(req, resp);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute(Constant.SESSION_ACCOUNT, user);
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + "/session/profile");
        } else {
            req.setAttribute("alert", "Tai khoan hoac mat khau khong dung");
            req.getRequestDispatcher("/views/session/login.jsp").forward(req, resp);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
