package gruporas.dttabelatarifaagua.shared.exception;

import java.io.Serial;
import java.io.Serializable;

public record FieldError(String name, String message) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
