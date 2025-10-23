package co.edu.uis.lunchuis.identityservice.web.controller;

import co.edu.uis.lunchuis.common.dto.ErrorResponse;
import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpAdminRequest;
import co.edu.uis.lunchuis.identityservice.application.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Endpoints for administrative operations")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user",
            description = "Creates a new user with a specified role. Only accessible by administrators.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{ \"message\": \"User registered successfully!\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data (e.g., failed @Valid constraints)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid field: email\" }")
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User does not have ADMIN role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Access Denied\", \"details\": \"User must have ADMIN role\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with given email or institutional code already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Conflict\", \"details\": \"Email 'test@example.com' already in use.\" }")
                    )
            )
    })
    public ResponseEntity<MessageResponse> createUser(
            @Valid @RequestBody SignUpAdminRequest request) {
        MessageResponse response = authenticationService.signupadmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
