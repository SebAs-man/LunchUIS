package co.edu.uis.lunchuis.identityservice.web.controller;

import co.edu.uis.lunchuis.common.dto.ErrorResponse;
import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRoleRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserResponseDTO;
import co.edu.uis.lunchuis.identityservice.application.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for user management operations.
 * Provides endpoints for administrators to manage users in the system.
 */
@RestController
@RequestMapping("auth/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user management operations (Admin only)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    private final UserManagementService userManagementService;
    
    // ========== READ OPERATIONS ==========
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieves all users in the system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all users",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(value = "[{\"id\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\",\"firstName\":\"Carlos\",\"lastName\":\"Beltrán\",\"institutionalCode\":2180001,\"email\":\"carlos.beltran@uis.edu.co\",\"role\":\"STUDENT\",\"enabled\":true}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User does not have ADMIN role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userManagementService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User does not have ADMIN role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User's unique identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable UUID id) {
        UserResponseDTO user = userManagementService.findUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/code/{institutionalCode}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by institutional code", description = "Retrieves a user by their institutional code")
    public ResponseEntity<UserResponseDTO> getUserByInstitutionalCode(
            @Parameter(description = "User's institutional code", example = "2180001")
            @PathVariable Integer institutionalCode) {
        UserResponseDTO user = userManagementService.findUserByInstitutionalCode(institutionalCode);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @Parameter(description = "User's email address", example = "carlos.beltran@uis.edu.co")
            @PathVariable String email) {
        UserResponseDTO user = userManagementService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/role/{roleType}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role", description = "Retrieves users by their role type")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @Parameter(description = "Role type", example = "STUDENT")
            @PathVariable RoleType roleType) {
        List<UserResponseDTO> users = userManagementService.findUsersByRole(roleType);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/enabled/{enabled}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by enabled status", description = "Retrieves users by their enabled status")
    public ResponseEntity<List<UserResponseDTO>> getUsersByEnabled(
            @Parameter(description = "Enabled status", example = "true")
            @PathVariable Boolean enabled) {
        List<UserResponseDTO> users = userManagementService.findUsersByEnabled(enabled);
        return ResponseEntity.ok(users);
    }
    
    // ========== UPDATE OPERATIONS ==========
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user", description = "Updates a user's information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
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
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User's unique identifier")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponseDTO updatedUser = userManagementService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role", description = "Updates a user's role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @Parameter(description = "User's unique identifier")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        UserResponseDTO updatedUser = userManagementService.updateUserRole(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user status", description = "Updates a user's enabled status")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @Parameter(description = "User's unique identifier")
            @PathVariable UUID id,
            @Parameter(description = "New enabled status")
            @RequestParam Boolean enabled) {
        UserResponseDTO updatedUser = userManagementService.updateUserStatus(id, enabled);
        return ResponseEntity.ok(updatedUser);
    }
    
    // ========== DELETE OPERATIONS ==========
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"User deleted successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User does not have ADMIN role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<MessageResponse> deleteUser(
            @Parameter(description = "User's unique identifier")
            @PathVariable UUID id) {
        MessageResponse response = userManagementService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
