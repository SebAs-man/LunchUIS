package co.edu.uis.lunchuis.identityservice.application.service;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.application.dto.request.ChangePasswordRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateProfileRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRoleRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserProfileResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing user operations.
 * Provides methods for user CRUD operations, profile management, and password changes.
 */
public interface UserManagementService {
    
    // ========== READ OPERATIONS ==========
    
    /**
     * Retrieves all users in the system.
     * @return List of all users
     */
    List<UserResponseDTO> findAllUsers();
    
    /**
     * Retrieves a user by their unique identifier.
     * @param id The user's unique identifier
     * @return User information
     */
    UserResponseDTO findUserById(UUID id);
    
    /**
     * Retrieves a user by their institutional code.
     * @param institutionalCode The user's institutional code
     * @return User information
     */
    UserResponseDTO findUserByInstitutionalCode(Integer institutionalCode);
    
    /**
     * Retrieves a user by their email address.
     * @param email The user's email address
     * @return User information
     */
    UserResponseDTO findUserByEmail(String email);
    
    /**
     * Retrieves users by their role.
     * @param roleType The role type to search for
     * @return List of users with the specified role
     */
    List<UserResponseDTO> findUsersByRole(RoleType roleType);
    
    /**
     * Retrieves users by their enabled status.
     * @param enabled The enabled status to search for
     * @return List of users with the specified enabled status
     */
    List<UserResponseDTO> findUsersByEnabled(Boolean enabled);
    
    /**
     * Retrieves the current user's profile information.
     * @return Current user's profile
     */
    UserProfileResponse getCurrentUserProfile();
    
    // ========== UPDATE OPERATIONS ==========
    
    /**
     * Updates a user's information (admin only).
     * @param id The user's unique identifier
     * @param request The update request containing new user information
     * @return Updated user information
     */
    UserResponseDTO updateUser(UUID id, UpdateUserRequest request);
    
    /**
     * Updates a user's role (admin only).
     * @param id The user's unique identifier
     * @param request The update request containing the new role
     * @return Updated user information
     */
    UserResponseDTO updateUserRole(UUID id, UpdateUserRoleRequest request);
    
    /**
     * Updates a user's enabled status (admin only).
     * @param id The user's unique identifier
     * @param enabled The new enabled status
     * @return Updated user information
     */
    UserResponseDTO updateUserStatus(UUID id, Boolean enabled);
    
    /**
     * Updates the current user's profile information.
     * @param request The update request containing new profile information
     * @return Updated profile information
     */
    UserProfileResponse updateProfile(UpdateProfileRequest request);
    
    /**
     * Changes the current user's password.
     * @param request The change password request containing current and new passwords
     * @return Success message
     */
    MessageResponse changePassword(ChangePasswordRequest request);
    
    // ========== DELETE OPERATIONS ==========
    
    /**
     * Deletes a user by their unique identifier (admin only).
     * @param id The user's unique identifier
     * @return Success message
     */
    MessageResponse deleteUser(UUID id);
    
    /**
     * Deletes the current user's account.
     * @return Success message
     */
    MessageResponse deleteCurrentUser();
}
