package co.edu.uis.lunchuis.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for cases where a resource creation fails due to a duplicate.
 */
public class DuplicateResourceException extends DomainException {
    /**
     * Constructs a new DuplicateResourceException to indicate that a resource creation
     * failed due to a duplicate entry. This exception encapsulates details about
     * the resource, the conflicting field, and the invalid value.
     * @param resource the name of the resource that caused the duplication conflict
     * @param field the name of the field causing the duplication issue
     * @param value the value of the conflicting field
     */
    public DuplicateResourceException(String resource, String field, Object value) {
        super("DUPLICATE_RESOURCE",
                String.format("%s already exists with %s: %s", resource, field, value),
                HttpStatus.CONFLICT);
    }
}
