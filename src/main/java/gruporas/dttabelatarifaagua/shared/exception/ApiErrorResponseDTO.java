package gruporas.dttabelatarifaagua.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponseDTO(
    String title,
    int status,
    String detail,
    String instance,
    List<FieldError> fieldErrors
) {}
