package co.edu.uis.lunchuis.identityservice.application.dto.request;

import co.edu.uis.lunchuis.common.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

/**
 * DTO for creating a new user by an administrator.
 * Includes all necessary user details and the role to be assigned.
 */
@Builder
@Schema(description = "Request DTO for creating a new user by an administrator.")
public record AdminUserCreationRequest(
        @NotBlank(message = "First name cannot be blank")
        @Schema(description = "User's first name.", example = "Admin")
        String firstName,
        @NotBlank(message = "Last name cannot be blank")
        @Schema(description = "User's last name.", example = "User")
        String lastName,
        @NotNull(message = "Institutional code cannot be null")
        @Schema(description = "User's unique institutional code.", example = "2180001")
        @Min(value = 1000000, message = "Institutional code must be at least 7 digits")
        @Max(value = 99999999, message = "Institutional code must be at most 8 digits")
        Integer institutionalCode,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        @Schema(description = "User's unique institutional email.", example = "admin@lunchuis.com")
        String email,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace.")
        @Schema(description = "User's password.", example = "Password123!")
        String password,
        @NotNull(message = "Role cannot be null")
        @Schema(description = "The role to assign to the new user.", example = "STUDENT")
        RoleType roleType
) {}
