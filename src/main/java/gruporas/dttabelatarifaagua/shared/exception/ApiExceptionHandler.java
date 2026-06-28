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
        String title = getLocalizedMessage(VALIDATION_ERROR_TITLE);
        String detail = "The request body is invalid or could not be parsed. Please verify the JSON syntax and field types";

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException cause) {
            String fieldName = cause.getPath().get(0).getFieldName();
            detail = String.format("Invalid value for field '%s'. Please check the allowed values.", fieldName);
        }

        ApiErrorResponse error = new ApiErrorResponse(title, HttpStatus.BAD_REQUEST.value(), detail);
        return ResponseEntity.status(status).body(error);
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
    public ResponseEntity<ApiErrorResponse> handle(ValidationException ex) {
        String title = getLocalizedMessage(VALIDATION_ERROR_TITLE);
        String detail = getLocalizedMessage(ex.getCode(), ex.getArgs());
        ApiErrorResponse error = new ApiErrorResponse(title, HttpStatus.BAD_REQUEST.value(), detail);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(EntityNotFoundException ex) {
        String title = getLocalizedMessage("error.notFound");
        String detail = getLocalizedMessage(ex.getCode());
        ApiErrorResponse error = new ApiErrorResponse(title, HttpStatus.NOT_FOUND.value(), detail);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(ResourceNotFoundException ex) {
        String title = getLocalizedMessage("error.notFound");
        String detail = getLocalizedMessage(ex.getCode());
        ApiErrorResponse error = new ApiErrorResponse(title, HttpStatus.NOT_FOUND.value(), detail);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    private String getLocalizedMessage(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (org.springframework.context.NoSuchMessageException e) {
            LOG.warn("Message code not found: {}", code);
            return code;
        }
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenException ex) {
        ApiErrorResponse error = new ApiErrorResponse("Access Denied", HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUnhandledExceptions(Exception ex, WebRequest request) {
        LOG.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ApiErrorResponse error = new ApiErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred. Please contact support if the issue persists");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
