package co.edu.uis.lunchuis.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate that an action is attempted without the necessary authorization.
 * This exception is typically used in scenarios where a user or process tries to execute an action
 * that they are not permitted to perform based on the current security policies or permissions.
 */
public class UnauthorizedActionException extends DomainException {
    /**
     * Constructs a new UnauthorizedActionException with the specified detail message.
     * This exception is thrown to indicate that an action is attempted without
     * the necessary authorization. Typically used in scenarios where a user or
     * process tries to perform an action without proper permission.
     * @param message the detail message describing the unauthorized action
     */
    public UnauthorizedActionException(String message) {
        super("UNAUTHORIZED_ACTION", message, HttpStatus.FORBIDDEN);
    }
}
