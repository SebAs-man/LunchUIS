package co.edu.uis.lunchuis.identityservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Data Transfer Object (DTO) for handling user login requests.
 * This class is designed to encapsulate the necessary information for a user to authenticate.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user login request.")
public final class LoginRequest {
    @Schema(description = "User's unique institutional code.", example = "2155917",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Institutional code cannot be null.")
    @Size(min = 6, max = 10, message = "Institutional code must be between 6 and 10 digits.")
    private Integer institutionalCode;

    @Schema(description = "User's password.", example = "password123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
