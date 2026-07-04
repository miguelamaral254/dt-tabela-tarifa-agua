package gruporas.dttabelatarifaagua.shared.factory;

import gruporas.dttabelatarifaagua.shared.exception.ApiErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Component
@RequiredArgsConstructor
public class ProblemDetailFactory {
    private final MessageSource messageSource;
    
    public ApiErrorResponseDTO create(HttpStatus status, String detailCode, WebRequest request, Object... args) {
        String instance = (request instanceof ServletWebRequest) ? ((ServletWebRequest) request).getRequest().getRequestURI() : null;
        return new ApiErrorResponseDTO(
                status.getReasonPhrase(),
                status.value(),
                getLocalizedMessage(detailCode, args),
                instance,
                null
        );
    }
    
    private String getLocalizedMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
