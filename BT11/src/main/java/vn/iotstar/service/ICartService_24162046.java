package vn.iotstar.service;

import vn.iotstar.entity.CartItem_24162046;

import java.math.BigDecimal;
import java.util.List;

public interface ICartService_24162046 {

    List<CartItem_24162046> getItems(int userid);

    /** So dong (so dau sach) trong gio. */
    long countItems(int userid);

    BigDecimal getTotal(List<CartItem_24162046> items);

    /**
     * Them sach vao gio, neu da co thi cong don so luong.
     * @throws IllegalArgumentException neu het hang / vuot gioi han
     */
    void addToCart(int userid, int bookid, int quantity);

    /**
     * Doi so luong 1 dong trong gio (1..gioi han).
     * @throws IllegalArgumentException neu so luong khong hop le
     */
    void updateQuantity(int userid, int itemId, int quantity);

    void remove(int userid, int itemId);

    void clear(int userid);
}
