package co.edu.uis.lunchuis.identityservice.web.controller;

import co.edu.uis.lunchuis.common.dto.ErrorResponse;
import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.request.ChangePasswordRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateProfileRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserProfileResponse;
import co.edu.uis.lunchuis.identityservice.application.service.UserManagementService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user profile management operations.
 * Provides endpoints for users to manage their own profile information.
 */
@RestController
@RequestMapping("auth/profile")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for user profile management")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    
    private final UserManagementService userManagementService;
    
    // ========== READ OPERATIONS ==========
    
    @GetMapping
    @Operation(summary = "Get current user profile", description = "Retrieves the current user's profile information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileResponse.class),
                            examples = @ExampleObject(value = "{\"id\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\",\"firstName\":\"Carlos\",\"lastName\":\"Beltrán\",\"institutionalCode\":2180001,\"email\":\"carlos.beltran@uis.edu.co\",\"role\":\"STUDENT\",\"enabled\":true}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<UserProfileResponse> getProfile() {
        UserProfileResponse profile = userManagementService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }
    
    // ========== UPDATE OPERATIONS ==========
    
    @PutMapping
    @Operation(summary = "Update profile", description = "Updates the current user's profile information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-01T00:00:00Z\",\"status\":400,\"code\":\"VALIDATION_ERROR\",\"message\":\"Validation failed for one or more fields\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-01T00:00:00Z\",\"status\":409,\"code\":\"DUPLICATE_RESOURCE\",\"message\":\"User already exists with email: test@example.com\"}")
                    )
            )
    })
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updatedProfile = userManagementService.updateProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
    
    @PatchMapping("/password")
    @Operation(summary = "Change password", description = "Changes the current user's password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password changed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"Password changed successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or password mismatch",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-01T00:00:00Z\",\"status\":400,\"code\":\"INVALID_REQUEST\",\"message\":\"New password and confirm password do not match\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid current password or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2024-01-01T00:00:00Z\",\"status\":401,\"code\":\"UNAUTHORIZED_ACTION\",\"message\":\"Current password is incorrect\"}")
                    )
            )
    })
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        MessageResponse response = userManagementService.changePassword(request);
        return ResponseEntity.ok(response);
    }
    
    // ========== DELETE OPERATIONS ==========
    
    @DeleteMapping
    @Operation(summary = "Delete account", description = "Deletes the current user's account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"Account deleted successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<MessageResponse> deleteAccount() {
        MessageResponse response = userManagementService.deleteCurrentUser();
        return ResponseEntity.ok(response);
    }
}
