package gruporas.dttabelatarifaagua.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RangeConflictException extends RuntimeException {
    public RangeConflictException(String message) {
        super(message);
    }
}
