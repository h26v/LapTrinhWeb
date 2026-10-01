package vn.iotstar.service;

import vn.iotstar.entity.OrderStatus_24162046;
import vn.iotstar.entity.Order_24162046;
import vn.iotstar.util.PageResult_24162046;

import java.util.Map;

public interface IOrderService_24162046 {

    /**
     * Dat hang toan bo gio hang, thanh toan khi nhan hang (COD).
     * @throws IllegalArgumentException neu thong tin nhan hang sai
     * @throws IllegalStateException neu gio trong / khong du hang
     */
    Order_24162046 placeCodOrder(int userid, String receiverName, String phone, String address, String note);

    Order_24162046 findByIdAndUser(int orderId, int userid);

    /** Lich su don cua user, loc theo trang thai (null = tat ca), moi nhat truoc. */
    PageResult_24162046<Order_24162046> getPage(int userid, OrderStatus_24162046 status, int page, int size);

    /** Ma trang thai -> so don, du ca 8 trang thai (khong co don thi 0). */
    Map<String, Long> countByStatus(int userid);
}
