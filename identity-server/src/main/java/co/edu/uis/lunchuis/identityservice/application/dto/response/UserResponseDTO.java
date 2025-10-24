package co.edu.uis.lunchuis.identityservice.application.dto.response;

import co.edu.uis.lunchuis.common.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for user response containing user information without sensitive data.
 * This DTO is used when returning user data to clients, excluding password and other sensitive information.
 */
@Schema(description = "User information response DTO")
public record UserResponseDTO(
        @Schema(description = "Unique identifier of the user", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,
        
        @Schema(description = "User's first name", example = "Carlos")
        String firstName,
        
        @Schema(description = "User's last name", example = "Beltrán")
        String lastName,
        
        @Schema(description = "User's institutional code", example = "2180001")
        Integer institutionalCode,
        
        @Schema(description = "User's email address", example = "carlos.beltran@uis.edu.co")
        String email,
        
        @Schema(description = "User's role type", example = "STUDENT")
        RoleType role,
        
        @Schema(description = "Whether the user is enabled", example = "true")
        Boolean enabled,
        
        @Schema(description = "Timestamp when the user was created")
        Instant createdAt,
        
        @Schema(description = "Timestamp when the user was last updated")
        Instant updatedAt
) {}
