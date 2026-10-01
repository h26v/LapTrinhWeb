package vn.iotstar.repository;

import vn.iotstar.entity.CartItem_24162046;

import java.util.List;

public interface ICartRepository_24162046 {

    List<CartItem_24162046> findByUser(int userid);

    CartItem_24162046 findByUserAndBook(int userid, int bookid);

    /** Chi tra ve neu dong gio hang thuoc ve user nay. */
    CartItem_24162046 findByIdAndUser(int id, int userid);

    long countByUser(int userid);

    void insert(int userid, int bookid, int quantity);

    void updateQuantity(int id, int quantity);

    void delete(int id, int userid);

    void deleteByUser(int userid);
}
