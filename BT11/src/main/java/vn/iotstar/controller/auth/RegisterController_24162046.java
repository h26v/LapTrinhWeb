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
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/register")
public class RegisterController_24162046 extends HttpServlet {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final IUserService_24162046 userService = new UserService_24162046();
    private final IEmailService_24162046 emailService = new EmailService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = ParamUtil_24162046.trim(req.getParameter("email"));
        String fullname = ParamUtil_24162046.trim(req.getParameter("fullname"));
        String phoneStr = ParamUtil_24162046.trim(req.getParameter("phone"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        String error = null;
        Integer phone = null;
        if (ParamUtil_24162046.isBlank(email) || !EMAIL.matcher(email).matches() || email.length() > 50) {
            error = "Email không hợp lệ";
        } else if (ParamUtil_24162046.isBlank(fullname) || fullname.length() > 50) {
            error = "Họ tên không được trống và tối đa 50 ký tự";
        } else if (password == null || password.length() < 6) {
            error = "Mật khẩu phải có ít nhất 6 ký tự";
        } else if (!password.equals(confirm)) {
            error = "Xác nhận mật khẩu không khớp";
        } else if (userService.isEmailRegistered(email)) {
            error = "Email đã được đăng ký";
        }
        if (error == null && !ParamUtil_24162046.isBlank(phoneStr)) {
            try {
                phone = Integer.valueOf(phoneStr);
            } catch (NumberFormatException e) {
                error = "Số điện thoại không hợp lệ";
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("email", email);
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phoneStr);
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        // Chua luu DB: giu user tam + OTP trong session, cho xac thuc
        User_24162046 pending = userService.buildPendingUser(email, fullname, phone, password);
        String otp = userService.generateOtp();
        HttpSession session = req.getSession(true);
        session.setAttribute(Constant_24162046.SESSION_PENDING_USER, pending);
        session.setAttribute(Constant_24162046.SESSION_OTP, otp);
        session.setAttribute(Constant_24162046.SESSION_OTP_EXPIRE,
                System.currentTimeMillis() + Constant_24162046.OTP_EXPIRE_MINUTES * 60_000L);

        boolean sent = emailService.sendOtp(email, otp);
        session.setAttribute("otpMessage", sent
                ? "Mã OTP đã được gửi tới " + email
                : "Không gửi được email (kiểm tra cấu hình Gmail). Mã OTP đã được in ra console server.");
        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }
}
