package gruporas.dttabelatarifaagua.shared.factory;

import gruporas.dttabelatarifaagua.shared.exception.ApiProblemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblemDetailFactory {
    private final MessageSource messageSource;
    
    public ApiProblemDetail create(HttpStatus status, String detailCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setDetail(getLocalizedMessage(detailCode));
        return new ApiProblemDetail(problemDetail);
    }

    public ApiProblemDetail create(HttpStatus status, String detailCode, Object... args) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setDetail(getLocalizedMessage(detailCode, args));
        return new ApiProblemDetail(problemDetail);
    }
    
    private String getLocalizedMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
