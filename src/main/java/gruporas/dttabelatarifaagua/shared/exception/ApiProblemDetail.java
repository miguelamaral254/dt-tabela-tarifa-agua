package gruporas.dttabelatarifaagua.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ProblemDetail;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiProblemDetail(ProblemDetail problemDetail, List<FieldError> fieldErrors) {
    public ApiProblemDetail(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }
}
