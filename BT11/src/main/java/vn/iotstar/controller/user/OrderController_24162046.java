package vn.iotstar.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.OrderStatus_24162046;
import vn.iotstar.entity.Order_24162046;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.service.IOrderService_24162046;
import vn.iotstar.service.impl.OrderService_24162046;
import vn.iotstar.util.Constant_24162046;
import vn.iotstar.util.ParamUtil_24162046;
import vn.iotstar.util.SessionUtil_24162046;

import java.io.IOException;
import java.util.Map;

/**
 * Lich su dat hang (phai dang nhap - AuthFilter).
 * GET /orders?status=&page=  -> danh sach don, loc theo trang thai
 * GET /orders/detail?id=     -> chi tiet 1 don
 * Trang thai doi trong DB (cot orders.status), moi lan load trang deu doc lai tu DB.
 */
@WebServlet(urlPatterns = "/orders/*")
public class OrderController_24162046 extends HttpServlet {

    private final IOrderService_24162046 orderService = new OrderService_24162046();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24162046 user = SessionUtil_24162046.currentUser(req);

        if ("/detail".equals(req.getPathInfo())) {
            Order_24162046 order = orderService.findByIdAndUser(
                    ParamUtil_24162046.parseInt(req.getParameter("id"), -1), user.getId());
            if (order == null) {
                SessionUtil_24162046.noticeError(req, "Không tìm thấy đơn hàng");
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }
            req.setAttribute("order", order);
            req.setAttribute("flowSteps", OrderStatus_24162046.flow());
            req.getRequestDispatcher("/views/user/order-detail.jsp").forward(req, resp);
            return;
        }

        OrderStatus_24162046 status = OrderStatus_24162046.fromCode(req.getParameter("status"));
        int page = ParamUtil_24162046.parseInt(req.getParameter("page"), 1);
        Map<String, Long> counts = orderService.countByStatus(user.getId());

        req.setAttribute("statuses", OrderStatus_24162046.values());
        req.setAttribute("currentStatus", status);
        req.setAttribute("counts", counts);
        req.setAttribute("totalOrders", counts.values().stream().mapToLong(Long::longValue).sum());
        req.setAttribute("result", orderService.getPage(user.getId(), status, page, Constant_24162046.ORDER_PAGE_SIZE));
        req.setAttribute("pageUrl", req.getContextPath() + "/orders?status="
                + (status == null ? "" : status.getCode()) + "&page=");
        req.getRequestDispatcher("/views/user/orders.jsp").forward(req, resp);
    }
}
