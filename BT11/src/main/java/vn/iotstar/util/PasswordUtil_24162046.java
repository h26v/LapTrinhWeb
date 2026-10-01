package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Cot passwd la varchar(32) nen luu MD5 dang hex (dung 32 ky tu).
 */
public final class PasswordUtil_24162046 {

    private PasswordUtil_24162046() {
    }

    public static String hash(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean matches(String raw, String hashed) {
        return raw != null && hashed != null && hash(raw).equalsIgnoreCase(hashed);
    }
}
