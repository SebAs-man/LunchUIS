package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating user profile information.
 * This DTO is used when users want to update their own profile information.
 */
@Schema(description = "DTO for updating user profile")
public record UpdateProfileRequest(
        @Schema(description = "User's first name", example = "Carlos")
        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,
        
        @Schema(description = "User's last name", example = "Beltrán")
        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,
        
        @Schema(description = "User's email address", example = "carlos.beltran@uis.edu.co")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email
) {}
