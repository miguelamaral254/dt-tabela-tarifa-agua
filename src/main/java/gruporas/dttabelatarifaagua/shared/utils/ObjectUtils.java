package gruporas.dttabelatarifaagua.core.shared.utils;

import gruporas.dttabelatarifaagua.core.shared.exception.ValidationException;

import java.util.Objects;

public class ObjectUtils {

    static void requireNonNull(Object value, String message) {
        if(Object.isNull(value)) {
           throw new ValidationException(message)
        }

    }
}
