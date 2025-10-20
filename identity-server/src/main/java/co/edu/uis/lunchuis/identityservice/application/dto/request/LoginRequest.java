package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO (record) for user login request.
 * Implements Jakarta Bean Validation constraints.
 *
 * @param institutionalCode  User's institutional code. Must not be blank.
 * @param password  User's password. Must not be blank and min 8 chars.
 * @author CodingPartner
 */

@Schema(description = "DTO for user login request.")
public record LoginRequest(
        @Schema(description = "User's unique institutional code.", example = "2155917",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Institutional code cannot be null.")
        @Min(value = 1000000, message = "Institutional code must be at least 7 digits")
        @Max(value = 99999999, message = "Institutional code must be at most 8 digits")
        Integer institutionalCode,
        @Schema(description = "User's password.", example = "password123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        String password
) {}
