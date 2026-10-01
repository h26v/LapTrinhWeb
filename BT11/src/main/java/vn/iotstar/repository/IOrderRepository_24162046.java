package vn.iotstar.repository;

import vn.iotstar.entity.OrderStatus_24162046;
import vn.iotstar.entity.Order_24162046;

import java.util.List;
import java.util.Map;

public interface IOrderRepository_24162046 {

    /**
     * Tao don tu gio hang cua user trong 1 transaction:
     * tru kho tung sach, luu don + chi tiet, xoa gio hang.
     * @throws IllegalStateException neu gio trong / khong du hang (rollback het)
     */
    void createFromCart(int userid, Order_24162046 order);

    /** Lay don kem chi tiet, chi tra ve neu don thuoc ve user nay. */
    Order_24162046 findByIdAndUser(int orderId, int userid);

    /** 1 trang don cua user (moi nhat truoc), kem chi tiet. status = null -> tat ca. */
    List<Order_24162046> findByUser(int userid, OrderStatus_24162046 status, int offset, int limit);

    long countByUser(int userid, OrderStatus_24162046 status);

    /** Trang thai -> so don cua user (trang thai nao khong co don thi khong co trong map). */
    Map<OrderStatus_24162046, Long> countByStatus(int userid);
}
