package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Record representing the sign-up request DTO for user registration.
 * This record holds the necessary details for creating a new user, including
 * validation constraints to ensure data integrity and proper formatting.
 * Fields:
 * - {@code firstName}: The first name of the user. Must not be blank.
 * - {@code lastName}: The last name of the user. Must not be blank.
 * - {@code institutionalCode}: The user's unique institutional code. Must be
 *   a non-null integer with a length between 6 and 10 digits.
 * - {@code email}: The user's institutional email address. Must not be blank
 *   and must follow a valid email format.
 * - {@code password}: The user's login password. Must not be blank and
 *   must have a minimum length of 8 characters.
 * This DTO is used in the user registration process and validated
 * against the defined constraints to ensure data consistency before
 * persistence into the system.
 */
@Schema(description = "DTO for user sign-up request.")
public record SignUpRequest (
        @Schema(description = "User's first name.", example = "Carlos",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "First name cannot be blank.")
        String firstName,
        @Schema(description = "User's last name.", example = "Beltrán",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name cannot be blank.")
        String lastName,
        @Schema(description = "User's unique institutional code.", example = "2180001",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Institutional code cannot be null.")
        @Min(value = 1000000, message = "Institutional code must be at least 7 digits")
        @Max(value = 99999999, message = "Institutional code must be at most 8 digits")
        Integer institutionalCode,
        @Schema(description = "User's unique institutional email.", example = "carlos.beltran@uis.edu.co",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Email should be valid.")
        String email,
        @Schema(description = "User's password. Must be at least 8 characters long.",
                example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must be at least 8 characters long.")
        String password
){}
