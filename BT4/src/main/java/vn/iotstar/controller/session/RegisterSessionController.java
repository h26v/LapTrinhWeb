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
import vn.iotstar.util.ValidationUtil;

@WebServlet(urlPatterns = { "/session/register", "/session/register/verify",
        "/session/register/resend-otp" })
public class RegisterSessionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ACTIVATION_USER_ID = "activationUserId";
    private static final String ACTIVATION_EMAIL = "activationEmail";

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        if (uri.contains("verify")) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute(ACTIVATION_USER_ID) == null) {
                resp.sendRedirect(req.getContextPath() + "/session/register");
                return;
            }
            req.getRequestDispatcher("/views/session/register-verify.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/views/session/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        if (uri.contains("resend-otp")) {
            resendOtp(req, resp);
        } else if (uri.contains("verify")) {
            verifyOtp(req, resp);
        } else {
            register(req, resp);
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = ValidationUtil.trim(req.getParameter("username"));
        String email = ValidationUtil.trim(req.getParameter("email"));
        String fullname = ValidationUtil.trim(req.getParameter("fullname"));
        String phone = ValidationUtil.trim(req.getParameter("phone"));
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            validateRegister(username, email, fullname, phone, password, confirmPassword);
            User user = userService.register(username, email, fullname, phone, password);
            HttpSession session = req.getSession(true);
            session.setAttribute(ACTIVATION_USER_ID, user.getId());
            session.setAttribute(ACTIVATION_EMAIL, user.getEmail());
            resp.sendRedirect(req.getContextPath() + "/session/register/verify");
        } catch (Exception e) {
            getServletContext().log("Dang ky tai khoan that bai", e);
            forwardRegister(req, resp, username, email, fullname, phone, e.getMessage());
        }
    }

    private void verifyOtp(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = sessionValue(session, ACTIVATION_USER_ID);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/session/register");
            return;
        }

        String otp = ValidationUtil.trim(req.getParameter("otp"));
        if (!ValidationUtil.isOtp(otp)) {
            req.setAttribute("alert", "Ma OTP phai gom 6 chu so.");
            req.getRequestDispatcher("/views/session/register-verify.jsp").forward(req, resp);
            return;
        }
        if (userService.verifyActivationOtp(userId, otp)) {
            session.removeAttribute(ACTIVATION_USER_ID);
            session.removeAttribute(ACTIVATION_EMAIL);
            session.setAttribute("success", "Kich hoat tai khoan thanh cong. Vui long dang nhap.");
            resp.sendRedirect(req.getContextPath() + "/session/login");
            return;
        }
        req.setAttribute("alert", "Ma OTP khong dung hoac da het han.");
        req.getRequestDispatcher("/views/session/register-verify.jsp").forward(req, resp);
    }

    private void resendOtp(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = sessionValue(session, ACTIVATION_USER_ID);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/session/register");
            return;
        }
        try {
            userService.resendActivationOtp(userId);
            req.setAttribute("success", "Da gui lai ma OTP den email cua ban.");
        } catch (Exception e) {
            getServletContext().log("Gui lai OTP that bai", e);
            req.setAttribute("alert", e.getMessage());
        }
        req.getRequestDispatcher("/views/session/register-verify.jsp").forward(req, resp);
    }

    private void validateRegister(String username, String email, String fullname, String phone,
            String password, String confirmPassword) {
        ValidationUtil.requireLength(username, "Ten dang nhap", 1, 100);
        if (ValidationUtil.containsWhitespace(username)) {
            throw new IllegalArgumentException("Ten dang nhap khong duoc chua khoang trang.");
        }
        ValidationUtil.requireEmail(email);
        ValidationUtil.requireLength(fullname, "Ho va ten", 1, 255);
        ValidationUtil.requirePhone(phone);
        if (ValidationUtil.isBlank(password) || password.length() < 6) {
            throw new IllegalArgumentException("Mat khau phai co it nhat 6 ky tu.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mat khau xac nhan khong khop.");
        }
    }

    private void forwardRegister(HttpServletRequest req, HttpServletResponse resp, String username,
            String email, String fullname, String phone, String alert) throws ServletException, IOException {
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullname", fullname);
        req.setAttribute("phone", phone);
        req.setAttribute("alert", alert);
        req.getRequestDispatcher("/views/session/register.jsp").forward(req, resp);
    }

    private Integer sessionValue(HttpSession session, String name) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(name);
        return value instanceof Integer ? (Integer) value : null;
    }
}
