package co.edu.uis.lunchuis.identityservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Represents a response containing a JWT access token after successful authentication.
 * This DTO is used to communicate the generated token back to the client.
 * It provides the token that can be used for later requests to authorized endpoints.
 */
@Value
@Builder
@AllArgsConstructor
@Schema(description = "DTO for JWT authentication response.")
public class JwtAuthenticationResponse {
    @Schema(description = "The JWT access token.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token;
}
