package gruporas.dttabelatarifaagua.shared.utils;

import gruporas.dttabelatarifaagua.shared.exception.ValidationException;

public class StringUtils {
    public static void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new ValidationException(message);
        }
    }
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
