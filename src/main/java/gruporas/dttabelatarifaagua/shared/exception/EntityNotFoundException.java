package gruporas.dttabelatarifaagua.shared.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String code;

    public EntityNotFoundException(String code) {
        super(code,null,false,false);
        this.code = code;
    }
}
