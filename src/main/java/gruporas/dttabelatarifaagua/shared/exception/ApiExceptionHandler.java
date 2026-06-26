package gruporas.dttabelatarifaagua.shared.exception;

import gruporas.dttabelatarifaagua.shared.factory.ProblemDetailFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String VALIDATION_ERROR_TITLE = "error.validation.title";

    private final MessageSource messageSource;
    private final ProblemDetailFactory problemDetailFactory;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        var problemFields = fieldErrors.stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(VALIDATION_ERROR_TITLE);
        problemDetail.setDetail("Some fields are invalid. Please check the error details");

        return ResponseEntity.status(status).body(new ApiProblemDetail(problemDetail, problemFields));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request Body");
        problemDetail.setDetail("The request body is invalid or could not be parsed. Please verify the JSON syntax and field types");

        return ResponseEntity.status(status).body(new ApiProblemDetail(problemDetail));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Endpoint Not Found");
        problemDetail.setDetail(String.format("The requested endpoint %s was not found", ex.getRequestURL()));

        return ResponseEntity.status(status).body(new ApiProblemDetail(problemDetail));
    }

    @ExceptionHandler(ValidationException.class)
    public ApiProblemDetail handle(ValidationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(getLocalizedMessage(VALIDATION_ERROR_TITLE));
        problemDetail.setDetail(getLocalizedMessage(ex.getCode(), ex.getArgs()));
        return new ApiProblemDetail(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiProblemDetail handle(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(getLocalizedMessage("error.notFound"));
        problemDetail.setDetail(getLocalizedMessage(ex.getCode()));
        return new ApiProblemDetail(problemDetail);
    }

    private String getLocalizedMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiProblemDetail> handleForbidden(ForbiddenException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Access Denied");
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiProblemDetail(problemDetail));
    }

    @ExceptionHandler(Exception.class)
    public ApiProblemDetail handleAllUnhandledExceptions(Exception ex, WebRequest request) {
        LOG.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("An unexpected error occurred. Please contact support if the issue persists");
        return new ApiProblemDetail(problemDetail);
    }
}
