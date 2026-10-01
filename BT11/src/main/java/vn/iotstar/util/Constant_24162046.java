package vn.iotstar.util;

import java.io.File;

public final class Constant_24162046 {

    private Constant_24162046() {
    }

    // Thong tin hien thi o Footer
    public static final String FULL_NAME = "Nguyễn Đăng Khoa";
    public static final String MSSV = "24162046";
    public static final String MA_DE = "01";

    // Session keys
    public static final String SESSION_ACCOUNT = "account";
    public static final String SESSION_PENDING_USER = "pendingUser";
    public static final String SESSION_OTP = "otp";
    public static final String SESSION_OTP_EXPIRE = "otpExpire";
    public static final String SESSION_CART_COUNT = "cartCount";

    // Thong bao hien o dau trang (decorator user.jsp)
    public static final String SESSION_NOTICE = "notice";
    public static final String SESSION_NOTICE_ERROR = "noticeError";

    // Gio hang: moi sach chi duoc mua toi da 10 cuon (va khong vuot ton kho)
    public static final int CART_MAX_PER_ITEM = 10;

    // Thanh toan khi nhan hang
    public static final String PAYMENT_COD = "COD";

    // Phan trang
    public static final int HOME_PAGE_SIZE = 6;
    public static final int ADMIN_PAGE_SIZE = 5;
    public static final int ORDER_PAGE_SIZE = 5;

    // OTP
    public static final int OTP_EXPIRE_MINUTES = 5;

    // Gmail gui OTP: thay bang email va App Password (16 ky tu) cua ban
    public static final String MAIL_FROM = "your-email@gmail.com";
    public static final String MAIL_APP_PASSWORD = "your-app-password";

    // Thu muc luu anh bia upload
    public static final String UPLOAD_DIR = System.getProperty("user.home") + File.separator + "bookstore_uploads";
}
