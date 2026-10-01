package vn.iotstar.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.util.Constant_24162046;

import java.io.IOException;

/**
 * Chi admin moi vao duoc /admin/*. Khai bao trong web.xml (truoc Sitemesh).
 */
public class AdminFilter_24162046 extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        Object account = session == null ? null : session.getAttribute(Constant_24162046.SESSION_ACCOUNT);

        if (!(account instanceof User_24162046 user)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!user.isAdminRole()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        chain.doFilter(req, resp);
    }
}
