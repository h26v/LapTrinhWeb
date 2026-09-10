package vn.iotstar.config;

/**
 * Ten cac thuoc tinh dung trong HttpSession. Gom mot cho de tranh go sai.
 */
public final class SessionKeys {

    /** Doi tuong {@code vn.iotstar.entity.User} cua nguoi dang dang nhap. */
    public static final String ACCOUNT = "account";

    /** Thong bao hien thi mot lan (dung cho redirect). */
    public static final String FLASH_SUCCESS = "success";
    public static final String FLASH_ERROR = "error";

    /** URL se quay ve sau khi dang nhap thanh cong. */
    public static final String REDIRECT_AFTER_LOGIN = "redirectAfterLogin";

    private SessionKeys() {
    }
}
