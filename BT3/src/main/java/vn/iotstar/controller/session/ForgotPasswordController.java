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

@WebServlet(urlPatterns = { "/session/forgot-password", "/session/reset-password" })
public class ForgotPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String RESET_USER_ID = "resetUserId";
    private static final String RESET_EMAIL = "resetEmail";

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getRequestURI().contains("reset-password")) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute(RESET_USER_ID) == null) {
                resp.sendRedirect(req.getContextPath() + "/session/forgot-password");
                return;
            }
            req.getRequestDispatcher("/views/session/reset-password.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/views/session/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getRequestURI().contains("reset-password")) {
            resetPassword(req, resp);
        } else {
            requestReset(req, resp);
        }
    }

    private void requestReset(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = trim(req.getParameter("email"));
        if (email == null) {
            req.setAttribute("alert", "Vui long nhap email.");
            req.getRequestDispatcher("/views/session/forgot-password.jsp").forward(req, resp);
            return;
        }

        try {
            userService.requestPasswordReset(email);
            User user = userService.findByEmail(email);
            if (user != null && user.getActive() == 1) {
                HttpSession session = req.getSession(true);
                session.setAttribute(RESET_USER_ID, user.getId());
                session.setAttribute(RESET_EMAIL, user.getEmail());
                resp.sendRedirect(req.getContextPath() + "/session/reset-password");
                return;
            }
            req.setAttribute("success", "Neu email ton tai, he thong da gui OTP dat lai mat khau.");
        } catch (Exception e) {
            getServletContext().log("Gui OTP quen mat khau that bai", e);
            req.setAttribute("alert", e.getMessage());
        }
        req.getRequestDispatcher("/views/session/forgot-password.jsp").forward(req, resp);
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = sessionValue(session, RESET_USER_ID);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/session/forgot-password");
            return;
        }

        String otp = trim(req.getParameter("otp"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("alert", "Mat khau xac nhan khong khop.");
            req.getRequestDispatcher("/views/session/reset-password.jsp").forward(req, resp);
            return;
        }
        if (userService.resetPassword(userId, otp, password)) {
            session.removeAttribute(RESET_USER_ID);
            session.removeAttribute(RESET_EMAIL);
            session.setAttribute("success", "Dat lai mat khau thanh cong. Vui long dang nhap.");
            resp.sendRedirect(req.getContextPath() + "/session/login");
            return;
        }
        req.setAttribute("alert", "Ma OTP khong dung, da het han hoac mat khau moi qua ngan.");
        req.getRequestDispatcher("/views/session/reset-password.jsp").forward(req, resp);
    }

    private Integer sessionValue(HttpSession session, String name) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(name);
        return value instanceof Integer ? (Integer) value : null;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
