package vn.iotstar.util;

import java.math.BigDecimal;

/**
 * Tien ich kiem tra du lieu, giu tuong thich voi BT4.
 */
public final class ValidationUtil {
    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String PHONE_REGEX = "[0-9+() .-]{7,20}";

    private ValidationUtil() {
    }

    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean containsWhitespace(String value) {
        return value != null && value.matches(".*\\s+.*");
    }

    public static boolean isEmail(String value) {
        String email = trim(value);
        return email != null && email.length() <= 255 && email.matches(EMAIL_REGEX);
    }

    public static boolean isPhone(String value) {
        String phone = trim(value);
        return isBlank(phone) || phone.matches(PHONE_REGEX);
    }

    public static boolean isStatus(String value) {
        return "0".equals(value) || "1".equals(value);
    }

    public static int parseId(String value) {
        try {
            if (isBlank(value)) {
                return 0;
            }
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int requireId(String value, String fieldName) {
        int id = parseId(value);
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " khong hop le.");
        }
        return id;
    }

    public static int parseStatus(String value) {
        if (!isStatus(value)) {
            throw new IllegalArgumentException("Trang thai khong hop le.");
        }
        return Integer.parseInt(value);
    }

    public static BigDecimal parsePrice(String value) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Vui long nhap gia san pham.");
        }
        try {
            BigDecimal price = new BigDecimal(value.trim());
            if (price.signum() < 0) {
                throw new IllegalArgumentException("Gia san pham khong duoc am.");
            }
            if (price.precision() - price.scale() > 10 || price.scale() > 2) {
                throw new IllegalArgumentException("Gia san pham phai phu hop DECIMAL(12,2).");
            }
            return price;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Gia san pham khong hop le.");
        }
    }

    public static void requireLength(String value, String fieldName, int min, int max) {
        String text = trim(value);
        int length = text == null ? 0 : text.length();
        if (length < min || length > max) {
            throw new IllegalArgumentException(fieldName + " phai co tu " + min + " den " + max + " ky tu.");
        }
    }

    public static void requireMaxLength(String value, String fieldName, int max) {
        String text = trim(value);
        if (text != null && text.length() > max) {
            throw new IllegalArgumentException(fieldName + " khong duoc vuot qua " + max + " ky tu.");
        }
    }

    public static void requireEmail(String value) {
        if (!isEmail(value)) {
            throw new IllegalArgumentException("Email khong hop le.");
        }
    }

    public static void requirePhone(String value) {
        if (!isPhone(value)) {
            throw new IllegalArgumentException("So dien thoai khong hop le.");
        }
    }
}
