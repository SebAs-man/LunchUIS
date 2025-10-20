package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Data Transfer Object (DTO) for handling user sign-up requests.
 * This class contains user input required during the registration process.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user sign-up request.")
public final class SignUpRequest {
    @Schema(description = "User's first name.", example = "Carlos",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;

    @Schema(description = "User's last name.", example = "Beltrán",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;

    @Schema(description = "User's unique institutional code.", example = "2180001",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Institutional code cannot be null.")
    @Size(min = 6, max = 10, message = "Institutional code must be between 6 and 10 digits.")
    private Integer institutionalCode;

    @Schema(description = "User's unique institutional email.", example = "carlos.beltran@uis.edu.co",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email should be valid.")
    private String email;

    @Schema(description = "User's password. Must be at least 8 characters long.",
            example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
