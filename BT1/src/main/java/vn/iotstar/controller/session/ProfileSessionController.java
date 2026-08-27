package vn.iotstar.controller.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.iotstar.model.User;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/session/profile" })
public class ProfileSessionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            User u = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            req.setAttribute("account", u);
            req.getRequestDispatcher("/views/session/profile.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/session/login");
        }
    }
}
