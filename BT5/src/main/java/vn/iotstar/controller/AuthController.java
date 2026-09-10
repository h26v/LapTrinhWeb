package vn.iotstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.config.SessionKeys;
import vn.iotstar.entity.User;
import vn.iotstar.service.AuthService;
import vn.iotstar.util.ValidationUtil;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        User account = (User) session.getAttribute(SessionKeys.ACCOUNT);
        if (account != null) {
            // Da dang nhap thi khong hien form nua.
            return account.getRoleid() == User.ROLE_ADMIN
                    ? "redirect:/admin/categories"
                    : "redirect:/access-denied";
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request, Model model) {

        String trimmedUsername = ValidationUtil.trim(username);
        String error = validate(trimmedUsername, password);
        if (error != null) {
            return failLogin(model, trimmedUsername, error);
        }

        User user = authService.login(trimmedUsername, password);
        if (user == null) {
            return failLogin(model, trimmedUsername, "Tai khoan hoac mat khau khong dung, hoac tai khoan da bi khoa.");
        }

        // Chong session fixation: huy session cu va tao session moi.
        HttpSession oldSession = request.getSession(false);
        String redirect = oldSession == null
                ? null
                : (String) oldSession.getAttribute(SessionKeys.REDIRECT_AFTER_LOGIN);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionKeys.ACCOUNT, user);
        session.setMaxInactiveInterval(30 * 60);

        if (user.getRoleid() != User.ROLE_ADMIN) {
            return "redirect:/access-denied";
        }
        return redirect != null && redirect.startsWith("/admin")
                ? "redirect:" + redirect
                : "redirect:/admin/categories";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }

    private String validate(String username, String password) {
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

    private String failLogin(Model model, String username, String error) {
        model.addAttribute("alert", error);
        model.addAttribute("username", username);
        return "auth/login";
    }
}
