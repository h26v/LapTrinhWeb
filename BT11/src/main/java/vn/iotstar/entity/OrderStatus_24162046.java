package vn.iotstar.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Trang thai don hang, luu trong cot orders.status dang chuoi (NEW, CONFIRMED, ...).
 * Muon test thi vao DB: UPDATE orders SET status = 'SHIPPING' WHERE order_id = 1;
 */
public enum OrderStatus_24162046 {

    NEW("Đơn hàng mới", "secondary", "bi-receipt"),
    CONFIRMED("Đã xác nhận", "info", "bi-check2-circle"),
    PREPARING("Chuẩn bị hàng", "primary", "bi-box-seam"),
    SHIPPING("Đang vận chuyển", "warning", "bi-truck"),
    DELIVERING("Đang giao hàng", "warning", "bi-bicycle"),
    DELIVERED("Đã giao", "success", "bi-house-check"),
    CANCELLED("Đã hủy", "danger", "bi-x-circle"),
    RETURNED("Hoàn hàng", "dark", "bi-arrow-counterclockwise");

    private final String label;
    private final String color;
    private final String icon;

    OrderStatus_24162046(String label, String color, String icon) {
        this.label = label;
        this.color = color;
        this.icon = icon;
    }

    /** Ten trang thai tieng Viet. */
    public String getLabel() {
        return label;
    }

    /** Mau Bootstrap cho badge: text-bg-${status.color} */
    public String getColor() {
        return color;
    }

    /** Icon Bootstrap Icons. */
    public String getIcon() {
        return icon;
    }

    /** Ma luu trong DB, dung trong JSP: ${status.code} */
    public String getCode() {
        return name();
    }

    /** Thu tu buoc (1..6) tren thanh tien trinh. */
    public int getStep() {
        return ordinal() + 1;
    }

    /** Nam tren luong giao hang binh thuong (khong phai huy / hoan). */
    public boolean isInFlow() {
        return ordinal() <= DELIVERED.ordinal();
    }

    /** Cac buoc tu dat hang toi giao xong. */
    public static List<OrderStatus_24162046> flow() {
        return Arrays.stream(values()).filter(OrderStatus_24162046::isInFlow).toList();
    }

    /** Doi chuoi tren URL thanh trang thai, sai thi tra ve null. */
    public static OrderStatus_24162046 fromCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        try {
            return valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
