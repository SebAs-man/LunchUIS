package co.edu.uis.lunchuis.identityservice.application.service;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.request.LoginRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.JwtAuthenticationResponse;

/**
 * Service interface for handling user authentication and registration.
 */
public interface AuthenticationService {
    /**
     * Registers a new user in the system.
     * @param request The sign-up requests DTO containing user details.
     * @return A message response indicating the result of the registration.
     */
    MessageResponse signup(SignUpRequest request);

    /**
     * Authenticates a user and returns a JWT.
     * @param request The login request DTO containing user credentials.
     * @return A DTO containing the JWT access token.
     */
    JwtAuthenticationResponse login(LoginRequest request);
}
