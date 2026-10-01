package vn.iotstar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order_24162046 implements Serializable {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User_24162046 user;

    @Nationalized
    @Column(name = "receiver_name", length = 50, nullable = false)
    private String receiverName;

    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @Nationalized
    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Nationalized
    @Column(name = "note", length = 255)
    private String note;

    // Hien tai chi co COD (thanh toan khi nhan hang)
    @Column(name = "payment_method", length = 20, nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus_24162046 status;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<OrderItem_24162046> items = new ArrayList<>();

    public Order_24162046() {
    }

    public void addItem(OrderItem_24162046 item) {
        item.setOrder(this);
        items.add(item);
    }

    /** Tong so cuon trong don. */
    public int getTotalQuantity() {
        return items.stream().mapToInt(OrderItem_24162046::getQuantity).sum();
    }

    /** LocalDateTime khong dung duoc voi fmt:formatDate nen format san. */
    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(TIME_FORMAT);
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public User_24162046 getUser() {
        return user;
    }

    public void setUser(User_24162046 user) {
        this.user = user;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus_24162046 getStatus() {
        return status;
    }

    public void setStatus(OrderStatus_24162046 status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem_24162046> getItems() {
        return items;
    }

    public void setItems(List<OrderItem_24162046> items) {
        this.items = items;
    }
}
