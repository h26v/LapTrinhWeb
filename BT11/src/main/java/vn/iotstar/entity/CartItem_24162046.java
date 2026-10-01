package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 1 dong trong gio hang cua user. Moi user chi co 1 dong cho 1 cuon sach.
 */
@Entity
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(name = "UQ_cart_items_user_book", columnNames = {"userid", "bookid"}))
public class CartItem_24162046 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User_24162046 user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bookid", nullable = false)
    private Book_24162046 book;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "added_at", columnDefinition = "datetime")
    private LocalDateTime addedAt;

    public CartItem_24162046() {
    }

    /** Thanh tien = don gia * so luong. */
    public BigDecimal getSubtotal() {
        BigDecimal price = book == null || book.getPrice() == null ? BigDecimal.ZERO : book.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity == null ? 0 : quantity));
    }

    /** So luong toi da cho phep cua dong nay. */
    public int getMaxQuantity() {
        return book == null ? 0 : book.getMaxOrderQuantity();
    }

    /** Kho bi giam sau khi them vao gio -> so luong trong gio vuot muc cho phep. */
    public boolean isOverStock() {
        return quantity != null && quantity > getMaxQuantity();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User_24162046 getUser() {
        return user;
    }

    public void setUser(User_24162046 user) {
        this.user = user;
    }

    public Book_24162046 getBook() {
        return book;
    }

    public void setBook(Book_24162046 book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
