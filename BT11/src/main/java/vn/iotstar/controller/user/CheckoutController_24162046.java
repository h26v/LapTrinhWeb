package vn.iotstar.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.CartItem_24162046;
import vn.iotstar.entity.Order_24162046;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.service.ICartService_24162046;
import vn.iotstar.service.IOrderService_24162046;
import vn.iotstar.service.impl.CartService_24162046;
import vn.iotstar.service.impl.OrderService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;
import vn.iotstar.util.SessionUtil_24162046;

import java.io.IOException;
import java.util.List;

/**
 * Thanh toan COD (phai dang nhap - AuthFilter).
 * GET  /checkout             -> form thong tin nhan hang + tom tat gio
 * POST /checkout             -> dat hang
 * GET  /checkout/success?id= -> trang dat hang thanh cong
 */
@WebServlet(urlPatterns = {"/checkout", "/checkout/success"})
public class CheckoutController_24162046 extends HttpServlet {

    private final ICartService_24162046 cartService = new CartService_24162046();
    private final IOrderService_24162046 orderService = new OrderService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24162046 user = SessionUtil_24162046.currentUser(req);

        if ("/checkout/success".equals(req.getServletPath())) {
            Order_24162046 order = orderService.findByIdAndUser(
                    ParamUtil_24162046.parseInt(req.getParameter("id"), -1), user.getId());
            if (order == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            req.setAttribute("order", order);
            req.getRequestDispatcher("/views/user/order-success.jsp").forward(req, resp);
            return;
        }

        // Dien san thong tin tu tai khoan (cot phone la INT nen mat so 0 dau)
        req.setAttribute("receiverName", user.getFullname());
        req.setAttribute("phone", user.getPhone() == null ? "" : "0" + user.getPhone());
        showForm(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24162046 user = SessionUtil_24162046.currentUser(req);
        String receiverName = req.getParameter("receiverName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String note = req.getParameter("note");

        String error = null;
        Order_24162046 order = null;
        if (!Constant_24162046.PAYMENT_COD.equals(req.getParameter("paymentMethod"))) {
            error = "Hiện chỉ hỗ trợ thanh toán khi nhận hàng (COD)";
        } else {
            try {
                order = orderService.placeCodOrder(user.getId(), receiverName, phone, address, note);
            } catch (IllegalArgumentException | IllegalStateException e) {
                error = e.getMessage();
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("receiverName", receiverName);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.setAttribute("note", note);
            showForm(req, resp, user);
            return;
        }

        SessionUtil_24162046.setCartCount(req, 0);
        resp.sendRedirect(req.getContextPath() + "/checkout/success?id=" + order.getOrderId());
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, User_24162046 user)
            throws ServletException, IOException {
        List<CartItem_24162046> items = cartService.getItems(user.getId());
        if (items.isEmpty()) {
            SessionUtil_24162046.noticeError(req, "Giỏ hàng đang trống, chọn sách trước đã nhé");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("items", items);
        req.setAttribute("total", cartService.getTotal(items));
        req.setAttribute("hasOverStock", items.stream().anyMatch(CartItem_24162046::isOverStock));
        req.getRequestDispatcher("/views/user/checkout.jsp").forward(req, resp);
    }
}
