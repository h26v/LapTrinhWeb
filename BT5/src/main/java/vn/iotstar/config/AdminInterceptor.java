package vn.iotstar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import vn.iotstar.entity.User;

/**
 * Chan moi truy cap vao /admin/** khi chua dang nhap hoac khong phai admin (roleid != 1).
 *
 * <p>Giu nguyen cach xac thuc cua BT3/BT4: doi tuong User duoc luu trong HttpSession.
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        User account = session == null ? null : (User) session.getAttribute(SessionKeys.ACCOUNT);

        if (account == null) {
            // Chua dang nhap: luu lai URL dang truy cap de quay ve sau khi dang nhap.
            String redirect = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                redirect = redirect + "?" + query;
            }
            session = request.getSession(true);
            session.setAttribute(SessionKeys.REDIRECT_AFTER_LOGIN, redirect);
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (account.getRoleid() != User.ROLE_ADMIN) {
            // Da dang nhap nhung khong phai admin.
            response.sendRedirect(request.getContextPath() + "/access-denied");
            return false;
        }

        return true;
    }
}
