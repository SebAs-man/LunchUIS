package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for changing user password.
 * This DTO is used when users want to change their password.
 */
@Schema(description = "DTO for changing user password")
public record ChangePasswordRequest(
        @Schema(description = "Current password", example = "currentPassword123")
        @NotBlank(message = "Current password cannot be blank")
        String currentPassword,
        
        @Schema(description = "New password", example = "newPassword123")
        @NotBlank(message = "New password cannot be blank")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "New password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
        String newPassword,
        
        @Schema(description = "Confirm new password", example = "newPassword123")
        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword
) {}
