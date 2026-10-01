package vn.iotstar.repository.impl;

import vn.iotstar.entity.Book_24162046;
import vn.iotstar.entity.CartItem_24162046;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.repository.ICartRepository_24162046;

import java.time.LocalDateTime;
import java.util.List;

public class CartRepository_24162046 extends AbstractRepository_24162046 implements ICartRepository_24162046 {

    @Override
    public List<CartItem_24162046> findByUser(int userid) {
        return query(em -> em.createQuery(
                        "SELECT c FROM CartItem_24162046 c JOIN FETCH c.book WHERE c.user.id = :uid ORDER BY c.id",
                        CartItem_24162046.class)
                .setParameter("uid", userid)
                .getResultList());
    }

    @Override
    public CartItem_24162046 findByUserAndBook(int userid, int bookid) {
        return query(em -> em.createQuery(
                        "SELECT c FROM CartItem_24162046 c JOIN FETCH c.book"
                                + " WHERE c.user.id = :uid AND c.book.bookid = :bid", CartItem_24162046.class)
                .setParameter("uid", userid)
                .setParameter("bid", bookid)
                .getResultStream()
                .findFirst()
                .orElse(null));
    }

    @Override
    public CartItem_24162046 findByIdAndUser(int id, int userid) {
        return query(em -> em.createQuery(
                        "SELECT c FROM CartItem_24162046 c JOIN FETCH c.book"
                                + " WHERE c.id = :id AND c.user.id = :uid", CartItem_24162046.class)
                .setParameter("id", id)
                .setParameter("uid", userid)
                .getResultStream()
                .findFirst()
                .orElse(null));
    }

    @Override
    public long countByUser(int userid) {
        return query(em -> em.createQuery(
                        "SELECT COUNT(c) FROM CartItem_24162046 c WHERE c.user.id = :uid", Long.class)
                .setParameter("uid", userid)
                .getSingleResult());
    }

    @Override
    public void insert(int userid, int bookid, int quantity) {
        transaction(em -> {
            CartItem_24162046 item = new CartItem_24162046();
            item.setUser(em.getReference(User_24162046.class, userid));
            item.setBook(em.getReference(Book_24162046.class, bookid));
            item.setQuantity(quantity);
            item.setAddedAt(LocalDateTime.now());
            em.persist(item);
        });
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        transaction(em -> {
            CartItem_24162046 item = em.find(CartItem_24162046.class, id);
            if (item != null) {
                item.setQuantity(quantity);
            }
        });
    }

    @Override
    public void delete(int id, int userid) {
        transaction(em -> em.createQuery(
                        "DELETE FROM CartItem_24162046 c WHERE c.id = :id AND c.user.id = :uid")
                .setParameter("id", id)
                .setParameter("uid", userid)
                .executeUpdate());
    }

    @Override
    public void deleteByUser(int userid) {
        transaction(em -> em.createQuery("DELETE FROM CartItem_24162046 c WHERE c.user.id = :uid")
                .setParameter("uid", userid)
                .executeUpdate());
    }
}
