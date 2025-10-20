package co.edu.uis.lunchuis.common.exception;

import co.edu.uis.lunchuis.common.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the LunchUIS Platform.
 * Intercepts exceptions of type {@link DomainException} and formats them
 * into a standardized {@link ErrorResponse} JSON object.
 * * By placing this in the 'common-library', all microservices that include
 * this library as a dependency will automatically inherit this exception handling,
 * ensuring consistent error responses across the platform.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // ¡AÑADE ESTA LÍNEA!
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles all custom domain-specific exceptions (subclasses of {@link DomainException}).
     * @param ex      The caught {@link DomainException}.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the standardized {@link ErrorResponse}
     * and the appropriate HTTP status code.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                ex.getStatus().value(),
                ex.getCode(),
                ex.getMessage(),
                this.getPath(request)
        );

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handles @Valid annotation failures (MethodArgumentNotValidException).
     * This method is overridden from ResponseEntityExceptionHandler to provide
     * our custom standardized ErrorResponse.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        // 1. Collect all field errors on a map
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        // 2. Create a standard error response
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Validation failed for one or more fields",
                getPath(request),
                errors
        );
        // 3. Returns the response
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other unhandled exception as a fallback.
     * This catches generic errors (like NullPointerException) and prevents
     * them from being returned as a 403 by Spring Security.
     * @param ex      The generic exception caught.
     * @param request The current web request.
     * @return A {@link ResponseEntity} with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please contact support.",
                getPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Helper to extract the request URI.
     */
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
