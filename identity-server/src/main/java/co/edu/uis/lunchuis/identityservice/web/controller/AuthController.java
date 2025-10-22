package co.edu.uis.lunchuis.identityservice.web.controller;

import co.edu.uis.lunchuis.common.dto.ErrorResponse;
import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.request.LoginRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.JwtAuthenticationResponse;
import co.edu.uis.lunchuis.identityservice.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user authentication and registration.
 * Implements REST endpoints for managing user access.
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login.")
public class AuthController {
    private final AuthenticationService service;

    /**
     * Registers a new user with the default role of STUDENT.
     * @param request The request body containing the new user's data
     * @return A message confirming successful registration
     */
    @Operation(summary = "Register a new user",
            description = "Creates a new user account with the default role of STUDENT.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"User registered successfully!\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid field: email\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists (email or institutional code)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"User already exists with institutional code: 12345\" }")
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(
            @Valid @RequestBody SignUpRequest request
    ){
        MessageResponse response = service.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user and returns a JWT upon successful login.
     * @param loginRequest The request body containing institutional code and password
     * @return A JWT authentication response containing the token
     */
    @Operation(summary = "Authenticate a user",
            description = "Authenticates a user with email and password, and returns a JWT upon success.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful, JWT returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class),
                            examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid institutional code or password\" }")
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }
}
