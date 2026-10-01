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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 1 dong trong don hang. Luu lai ten sach + gia luc dat
 * de sau nay admin sua gia / xoa sach thi don cu van dung.
 */
@Entity
@Table(name = "order_items")
public class OrderItem_24162046 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order_24162046 order;

    // NULL neu sach da bi admin xoa
    @ManyToOne
    @JoinColumn(name = "bookid")
    private Book_24162046 book;

    @Column(name = "book_title", length = 200, nullable = false)
    private String bookTitle;

    @Column(name = "price", precision = 6, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderItem_24162046() {
    }

    /** Thanh tien = gia luc dat * so luong. */
    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order_24162046 getOrder() {
        return order;
    }

    public void setOrder(Order_24162046 order) {
        this.order = order;
    }

    public Book_24162046 getBook() {
        return book;
    }

    public void setBook(Book_24162046 book) {
        this.book = book;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
