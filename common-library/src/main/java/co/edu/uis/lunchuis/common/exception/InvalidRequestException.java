package co.edu.uis.lunchuis.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request is syntactically correct
 * but semantically invalid (e.g., violates business rules or preconditions).
 */
public class InvalidRequestException extends DomainException {
    /**
     * Constructs a new InvalidRequestException with the specified detail message.
     * This exception is thrown when a request is syntactically correct but semantically invalid,
     * such as when it violates business rules or preconditions.
     * @param message the detail message describing the nature of the invalid request
     */
    public InvalidRequestException(String message) {
        super("INVALID_REQUEST", message, HttpStatus.BAD_REQUEST);
    }
}
