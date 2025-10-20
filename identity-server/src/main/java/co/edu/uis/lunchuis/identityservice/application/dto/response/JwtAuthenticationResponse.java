package co.edu.uis.lunchuis.identityservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a response DTO containing a JWT access token.
 * This record is used to encapsulate the authentication token
 * generated during the user login process. The token is returned
 * to the client upon a successful authentication as part of the authentication service.
 */
@Schema(description = "DTO for JWT authentication response.")
public record JwtAuthenticationResponse(
        @Schema(description = "The JWT access token.",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {}
