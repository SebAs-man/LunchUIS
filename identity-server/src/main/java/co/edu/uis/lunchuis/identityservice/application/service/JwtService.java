package co.edu.uis.lunchuis.identityservice.application.service;

import co.edu.uis.lunchuis.identityservice.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for handling JSON Web Tokens (JWT).
 * Defines contracts for generating, validating, and extracting information from tokens.
 */
public interface JwtService {
    /**
     * Extracts the institutional code from the JWT token.
     * @param token the JWT token
     * @return the institutional code stored in the token
     */
    String extractUsername(String token);

    /**
     * Generates a JWT token for the given user using the institutional code.
     * @param user the user for whom the token is generated
     * @return a signed JWT token
     */
    String generateToken(User user);

    /**
     * Validates a JWT.
     * Checks if the token belongs to the user and is not expired.
     * @param token       The JWT to validate.
     * @param userDetails The user details an object to validate against.
     * @return true if the token is valid, false otherwise.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
