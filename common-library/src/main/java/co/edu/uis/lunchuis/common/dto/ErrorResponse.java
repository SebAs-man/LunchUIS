package co.edu.uis.lunchuis.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a standardized error response body sent to the client.
 * This provides a consistent error format across all microservices.
 *
 * @param timestamp The exact time the error occurred.
 * @param status    The HTTP status code.
 * @param code      The application-specific error code (from DomainException).
 * @param message   The human-readable error message.
 * @param path      The request URI that triggered the error.
 * @param fieldErrors Validation errors per field
 */
@Schema(name = "ErrorResponse", description = "Standardized error response structure")
public record ErrorResponse(
        @Schema(description = "The timestamp when the error occurred", example = "2025-10-20T03:30:00.123456Z")
        Instant timestamp,
        @Schema(description = "HTTP Status Code", example = "409")
        int status,
        @Schema(description = "Application-specific error code", example = "DUPLICATE_RESOURCE")
        String code,
        @Schema(description = "Detailed error message", example = "Estudiante already exists with code: 2180001")
        String message,
        @Schema(description = "The request path that caused the error", example = "/api/v1/students")
        String path,
        @Schema(description = "Validation errors per field")
        Map<String, String> fieldErrors
) {
        /**
         * Constructor for domain exceptions (without field errors)
         */
        public ErrorResponse(Instant ts, int status, String code, String msg, String path) {
                this(ts, status, code, msg, path, null);
        }
}
