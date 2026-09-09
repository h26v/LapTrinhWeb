package vn.iotstar.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil() {
    }

    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt(12));
    }

    public static boolean matches(String raw, String stored) {
        if (raw == null || stored == null) {
            return false;
        }
        if (isBCrypt(stored)) {
            return BCrypt.checkpw(raw, stored);
        }
        // Tuong thich tai khoan demo cu dang luu plain text.
        return raw.equals(stored);
    }

    public static boolean isBCrypt(String stored) {
        return stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$")
                || stored.startsWith("$2y$"));
    }
}
