package gruporas.dttabelatarifaagua.shared.utils;

import gruporas.dttabelatarifaagua.shared.exception.ValidationException;

import java.util.Objects;

public class ObjectUtils {

    public static void requireNonNull(Object value, String message) {
        if(Objects.isNull(value)) {
           throw new ValidationException(message);
        }

    }
}

