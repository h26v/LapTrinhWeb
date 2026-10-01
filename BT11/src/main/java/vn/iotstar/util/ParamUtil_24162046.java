package vn.iotstar.util;

public final class ParamUtil_24162046 {

    private ParamUtil_24162046() {
    }

    public static int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Chi nhan duong dan noi bo dang "/abc?x=1" de redirect (chong open redirect).
     * Tra ve null neu khong hop le.
     */
    public static String safePath(String path) {
        if (isBlank(path)) {
            return null;
        }
        String p = path.trim();
        if (!p.startsWith("/") || p.startsWith("//") || p.contains("\\")
                || p.chars().anyMatch(ch -> ch < 0x20 || ch == 0x7f)) {
            return null;
        }
        return p;
    }
}
