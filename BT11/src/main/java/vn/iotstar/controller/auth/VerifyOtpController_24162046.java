package vn.iotstar.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.service.IEmailService_24162046;
import vn.iotstar.service.IUserService_24162046;
import vn.iotstar.service.impl.EmailService_24162046;
import vn.iotstar.service.impl.UserService_24162046;
import vn.iotstar.util.Constant_24162046;

import java.io.IOException;

@WebServlet(urlPatterns = "/verify-otp")
public class VerifyOtpController_24162046 extends HttpServlet {

    private final IUserService_24162046 userService = new UserService_24162046();
    private final IEmailService_24162046 emailService = new EmailService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (pendingUser(session) == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.setAttribute("pendingEmail", pendingUser(session).getEmail());
        req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User_24162046 pending = pendingUser(session);
        if (pending == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        // Gui lai OTP
        if ("resend".equals(req.getParameter("action"))) {
            String otp = userService.generateOtp();
            session.setAttribute(Constant_24162046.SESSION_OTP, otp);
            session.setAttribute(Constant_24162046.SESSION_OTP_EXPIRE,
                    System.currentTimeMillis() + Constant_24162046.OTP_EXPIRE_MINUTES * 60_000L);
            boolean sent = emailService.sendOtp(pending.getEmail(), otp);
            session.setAttribute("otpMessage", sent ? "Đã gửi lại mã OTP" : "Không gửi được email, xem console server");
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
            return;
        }

        String input = req.getParameter("otp");
        String otp = (String) session.getAttribute(Constant_24162046.SESSION_OTP);
        Long expire = (Long) session.getAttribute(Constant_24162046.SESSION_OTP_EXPIRE);

        String error = null;
        if (expire == null || System.currentTimeMillis() > expire) {
            error = "Mã OTP đã hết hạn, vui lòng bấm Gửi lại mã";
        } else if (input == null || !input.trim().equals(otp)) {
            error = "Mã OTP không đúng";
        }

        if (error == null) {
            try {
                userService.activate(pending);
            } catch (IllegalStateException e) {
                error = e.getMessage();
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("pendingEmail", pending.getEmail());
            req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
            return;
        }

        session.removeAttribute(Constant_24162046.SESSION_PENDING_USER);
        session.removeAttribute(Constant_24162046.SESSION_OTP);
        session.removeAttribute(Constant_24162046.SESSION_OTP_EXPIRE);
        session.removeAttribute("otpMessage");
        resp.sendRedirect(req.getContextPath() + "/login?registered=1");
    }

    private User_24162046 pendingUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User_24162046) session.getAttribute(Constant_24162046.SESSION_PENDING_USER);
    }
}
