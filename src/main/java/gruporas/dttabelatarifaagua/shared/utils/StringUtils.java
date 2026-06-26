package gruporas.dttabelatarifaagua.shared.utils;

import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import org.springframework.util.StringUtils;

public class StringUtils {
    static void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new ValidationException(message);
        }
    }
    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    static boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
