package co.edu.uis.lunchuis.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for all domain-related errors in the LunchUIS platform.
 * Provides standardized structure for error handling and HTTP response mapping.
 */
public abstract class DomainException extends RuntimeException{
    private final String code;
    private final HttpStatus status;

    /**
     * Constructs a new DomainException with the specified error code, detail message, and HTTP status.
     * Designed as a base exception to encapsulate domain-specific error information,
     * including a unique error code and corresponding HTTP status for response handling.
     * @param code the unique identifier for this specific domain error
     * @param message the detail message describing the nature of the exception
     * @param status the HTTP status code associated with this domain error
     */
    protected DomainException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    // --- Getters ---
    public String getCode() {
        return code;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
