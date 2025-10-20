package co.edu.uis.lunchuis.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * Commonly used across microservices for missing entities (e.g., users, orders, etc.).
 */
public class ResourceNotFoundException extends DomainException {
    /**
     * Constructs a new ResourceNotFoundException with detailed information about the missing resource.
     * This exception is typically used to indicate that a requested resource
     * could not be found based on the provided criteria.
     * @param resource the name of the resource that could not be found
     * @param field the name of the field used to search for the resource
     * @param value the value of the specified field used in the search
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super("Resource NOT found:",
                String.format("%s not found with %s: %s", resource, field, value),
                HttpStatus.NOT_FOUND);
    }
}
