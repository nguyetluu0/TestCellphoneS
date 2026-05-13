package utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Đọc tài khoản test từ {@code datn-test.properties} hoặc biến môi trường {@code DATN_PHONE}/{@code DATN_PASSWORD}.
 */
public final class TestCredentials {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = TestCredentials.class.getClassLoader().getResourceAsStream("datn-test.properties")) {
            if (is != null) {
                PROPS.load(is);
            }
        } catch (Exception ignored) {
        }
    }

    private TestCredentials() {}

    public static String phone() {
        String env = System.getenv("DATN_PHONE");
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return Objects.toString(PROPS.getProperty("datn.phone", ""), "").trim();
    }

    public static String password() {
        String env = System.getenv("DATN_PASSWORD");
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return Objects.toString(PROPS.getProperty("datn.password", ""), "").trim();
    }

    public static boolean isConfigured() {
        return !phone().isBlank() && !password().isBlank();
    }
}
