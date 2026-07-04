package gruporas.dttabelatarifaagua.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.ProblemDetail;
import java.net.URI;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiProblemDetail extends ProblemDetail {
    private final List<FieldError> fieldErrors;

    public ApiProblemDetail(ProblemDetail problemDetail, List<FieldError> fieldErrors) {
        super(problemDetail);
        this.fieldErrors = fieldErrors;
    }

    public ApiProblemDetail(ProblemDetail problemDetail) {
        this(problemDetail, null);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    @JsonIgnore
    public URI getType() {
        return super.getType();
    }
}
