package vn.iotstar.util;

import java.security.SecureRandom;
import java.util.Date;

public final class OtpUtil {
    public static final int OTP_MINUTES = 10;

    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil() {
    }

    public static String generateOtp() {
        int value = RANDOM.nextInt(1_000_000);
        return String.format("%06d", value);
    }

    public static Date expiresAt() {
        return new Date(System.currentTimeMillis() + OTP_MINUTES * 60L * 1000L);
    }

    public static boolean isExpired(Date expiresAt) {
        return expiresAt == null || expiresAt.before(new Date());
    }
}
