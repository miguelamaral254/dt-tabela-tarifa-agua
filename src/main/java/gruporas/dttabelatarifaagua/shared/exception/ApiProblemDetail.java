package gruporas.dttabelatarifaagua.shared.exception;

import org.springframework.http.ProblemDetail;
import java.util.List;

public record ApiProblemDetail(ProblemDetail problemDetail, List<FieldError> fieldErrors) {
    public ApiProblemDetail(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }
}
