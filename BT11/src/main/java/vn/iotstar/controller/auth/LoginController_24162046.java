package vn.iotstar.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.service.ICartService_24162046;
import vn.iotstar.service.IUserService_24162046;
import vn.iotstar.service.impl.CartService_24162046;
import vn.iotstar.service.impl.UserService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController_24162046 extends HttpServlet {

    private final IUserService_24162046 userService = new UserService_24162046();
    private final ICartService_24162046 cartService = new CartService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant_24162046.SESSION_ACCOUNT) instanceof User_24162046 user) {
            redirectByRole(req, resp, user);
            return;
        }
        req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User_24162046 user = userService.login(email, password);
        if (user == null) {
            // Dang nhap that bai -> quay lai trang dang nhap
            req.setAttribute("error", "Email hoặc mật khẩu không đúng");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
            return;
        }

        // Tao session moi tranh session fixation
        HttpSession old = req.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute(Constant_24162046.SESSION_ACCOUNT, user);
        session.setAttribute(Constant_24162046.SESSION_CART_COUNT, cartService.countItems(user.getId()));

        // Dang nhap tu trang gio hang / don hang... thi quay lai dung trang do
        String next = ParamUtil_24162046.safePath(req.getParameter("next"));
        if (next != null) {
            resp.sendRedirect(req.getContextPath() + next);
            return;
        }
        redirectByRole(req, resp, user);
    }

    private void redirectByRole(HttpServletRequest req, HttpServletResponse resp, User_24162046 user) throws IOException {
        if (user.isAdminRole()) {
            resp.sendRedirect(req.getContextPath() + "/admin/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
