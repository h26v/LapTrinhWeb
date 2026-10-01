package vn.iotstar.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.CartItem_24162046;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.service.ICartService_24162046;
import vn.iotstar.service.impl.CartService_24162046;
import vn.iotstar.util.ParamUtil_24162046;
import vn.iotstar.util.SessionUtil_24162046;

import java.io.IOException;
import java.util.List;

/**
 * Gio hang (phai dang nhap - AuthFilter).
 * GET  /cart          -> xem gio hang
 * POST /cart/add      -> them sach (bookid, quantity, back)
 * POST /cart/update   -> doi so luong (itemId, quantity)
 * POST /cart/remove   -> xoa 1 sach (itemId)
 * POST /cart/clear    -> xoa het gio
 */
@WebServlet(urlPatterns = "/cart/*")
public class CartController_24162046 extends HttpServlet {

    private final ICartService_24162046 cartService = new CartService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24162046 user = SessionUtil_24162046.currentUser(req);
        List<CartItem_24162046> items = cartService.getItems(user.getId());
        SessionUtil_24162046.setCartCount(req, items.size());

        req.setAttribute("items", items);
        req.setAttribute("total", cartService.getTotal(items));
        req.setAttribute("hasOverStock", items.stream().anyMatch(CartItem_24162046::isOverStock));
        req.getRequestDispatcher("/views/user/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User_24162046 user = SessionUtil_24162046.currentUser(req);
        String action = req.getPathInfo() == null ? "/" : req.getPathInfo();
        String redirect = "/cart";

        try {
            switch (action) {
                case "/add" -> {
                    cartService.addToCart(user.getId(),
                            ParamUtil_24162046.parseInt(req.getParameter("bookid"), -1),
                            ParamUtil_24162046.parseInt(req.getParameter("quantity"), 1));
                    SessionUtil_24162046.notice(req, "Đã thêm sách vào giỏ hàng");
                    String back = ParamUtil_24162046.safePath(req.getParameter("back"));
                    if (back != null) {
                        redirect = back;
                    }
                }
                case "/update" -> {
                    cartService.updateQuantity(user.getId(),
                            ParamUtil_24162046.parseInt(req.getParameter("itemId"), -1),
                            ParamUtil_24162046.parseInt(req.getParameter("quantity"), 0));
                    SessionUtil_24162046.notice(req, "Đã cập nhật số lượng");
                }
                case "/remove" -> {
                    cartService.remove(user.getId(), ParamUtil_24162046.parseInt(req.getParameter("itemId"), -1));
                    SessionUtil_24162046.notice(req, "Đã xóa sách khỏi giỏ hàng");
                }
                case "/clear" -> {
                    cartService.clear(user.getId());
                    SessionUtil_24162046.notice(req, "Đã xóa hết giỏ hàng");
                }
                default -> {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
        } catch (IllegalArgumentException e) {
            SessionUtil_24162046.noticeError(req, e.getMessage());
            // Them that bai thi quay lai trang dang xem
            String back = ParamUtil_24162046.safePath(req.getParameter("back"));
            if ("/add".equals(action) && back != null) {
                redirect = back;
            }
        }

        SessionUtil_24162046.setCartCount(req, cartService.countItems(user.getId()));
        resp.sendRedirect(req.getContextPath() + redirect);
    }
}
