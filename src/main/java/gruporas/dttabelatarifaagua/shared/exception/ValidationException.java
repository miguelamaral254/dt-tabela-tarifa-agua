package gruporas.dttabelatarifaagua.shared.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final String code;
    private final Object... args;
}
