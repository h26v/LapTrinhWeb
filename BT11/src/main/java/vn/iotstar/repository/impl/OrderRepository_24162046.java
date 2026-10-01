package vn.iotstar.repository.impl;

import vn.iotstar.entity.Book_24162046;
import vn.iotstar.entity.CartItem_24162046;
import jakarta.persistence.TypedQuery;
import vn.iotstar.entity.OrderItem_24162046;
import vn.iotstar.entity.OrderStatus_24162046;
import vn.iotstar.entity.Order_24162046;
import vn.iotstar.entity.User_24162046;
import vn.iotstar.repository.IOrderRepository_24162046;
import vn.iotstar.util.Constant_24162046;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class OrderRepository_24162046 extends AbstractRepository_24162046 implements IOrderRepository_24162046 {

    @Override
    public void createFromCart(int userid, Order_24162046 order) {
        transaction(em -> {
            List<CartItem_24162046> cart = em.createQuery(
                            "SELECT c FROM CartItem_24162046 c JOIN FETCH c.book WHERE c.user.id = :uid ORDER BY c.id",
                            CartItem_24162046.class)
                    .setParameter("uid", userid)
                    .getResultList();
            if (cart.isEmpty()) {
                throw new IllegalStateException("Giỏ hàng đang trống");
            }

            BigDecimal total = BigDecimal.ZERO;
            for (CartItem_24162046 c : cart) {
                Book_24162046 book = c.getBook();
                int qty = c.getQuantity();
                if (book.getPrice() == null) {
                    throw new IllegalStateException("Sách \"" + book.getTitle() + "\" chưa có giá bán");
                }
                if (qty > Constant_24162046.CART_MAX_PER_ITEM) {
                    throw new IllegalStateException("Mỗi sách mua tối đa " + Constant_24162046.CART_MAX_PER_ITEM + " cuốn");
                }
                // Tru kho co dieu kien: 2 nguoi dat cung luc cung khong bi am kho
                int updated = em.createQuery(
                                "UPDATE Book_24162046 b SET b.quantity = b.quantity - :qty"
                                        + " WHERE b.bookid = :id AND b.quantity >= :qty")
                        .setParameter("qty", qty)
                        .setParameter("id", book.getBookid())
                        .executeUpdate();
                if (updated == 0) {
                    throw new IllegalStateException("Sách \"" + book.getTitle() + "\" không đủ hàng (kho còn "
                            + book.getQuantity() + " cuốn), bạn chỉnh lại giỏ hàng giúp nhé");
                }

                OrderItem_24162046 item = new OrderItem_24162046();
                item.setBook(book);
                item.setBookTitle(book.getTitle());
                item.setPrice(book.getPrice());
                item.setQuantity(qty);
                order.addItem(item);
                total = total.add(item.getSubtotal());
            }

            order.setUser(em.getReference(User_24162046.class, userid));
            order.setTotalAmount(total);
            em.persist(order);

            em.createQuery("DELETE FROM CartItem_24162046 c WHERE c.user.id = :uid")
                    .setParameter("uid", userid)
                    .executeUpdate();
        });
    }

    @Override
    public Order_24162046 findByIdAndUser(int orderId, int userid) {
        return query(em -> em.createQuery(
                        "SELECT o FROM Order_24162046 o LEFT JOIN FETCH o.items"
                                + " WHERE o.orderId = :id AND o.user.id = :uid", Order_24162046.class)
                .setParameter("id", orderId)
                .setParameter("uid", userid)
                .getResultStream()
                .findFirst()
                .orElse(null));
    }

    @Override
    public List<Order_24162046> findByUser(int userid, OrderStatus_24162046 status, int offset, int limit) {
        return query(em -> {
            // Phan trang tren id truoc, roi moi JOIN FETCH items (phan trang thang tren JOIN FETCH se sai)
            TypedQuery<Integer> idQuery = em.createQuery(
                            "SELECT o.orderId FROM Order_24162046 o WHERE o.user.id = :uid"
                                    + (status == null ? "" : " AND o.status = :status")
                                    + " ORDER BY o.orderId DESC", Integer.class)
                    .setParameter("uid", userid);
            if (status != null) {
                idQuery.setParameter("status", status);
            }
            List<Integer> ids = idQuery.setFirstResult(offset).setMaxResults(limit).getResultList();
            if (ids.isEmpty()) {
                return List.of();
            }
            return em.createQuery(
                            "SELECT o FROM Order_24162046 o LEFT JOIN FETCH o.items"
                                    + " WHERE o.orderId IN :ids ORDER BY o.orderId DESC", Order_24162046.class)
                    .setParameter("ids", ids)
                    .getResultList();
        });
    }

    @Override
    public long countByUser(int userid, OrderStatus_24162046 status) {
        return query(em -> {
            TypedQuery<Long> q = em.createQuery(
                            "SELECT COUNT(o) FROM Order_24162046 o WHERE o.user.id = :uid"
                                    + (status == null ? "" : " AND o.status = :status"), Long.class)
                    .setParameter("uid", userid);
            if (status != null) {
                q.setParameter("status", status);
            }
            return q.getSingleResult();
        });
    }

    @Override
    public Map<OrderStatus_24162046, Long> countByStatus(int userid) {
        List<Object[]> rows = query(em -> em.createQuery(
                        "SELECT o.status, COUNT(o) FROM Order_24162046 o WHERE o.user.id = :uid GROUP BY o.status",
                        Object[].class)
                .setParameter("uid", userid)
                .getResultList());
        Map<OrderStatus_24162046, Long> result = new EnumMap<>(OrderStatus_24162046.class);
        for (Object[] row : rows) {
            result.put((OrderStatus_24162046) row[0], (Long) row[1]);
        }
        return result;
    }
}
