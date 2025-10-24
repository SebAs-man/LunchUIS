package co.edu.uis.lunchuis.identityservice.application.service.impl;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.common.exception.DuplicateResourceException;
import co.edu.uis.lunchuis.common.exception.InvalidRequestException;
import co.edu.uis.lunchuis.common.exception.ResourceNotFoundException;
import co.edu.uis.lunchuis.common.exception.UnauthorizedActionException;
import co.edu.uis.lunchuis.identityservice.application.dto.request.ChangePasswordRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateProfileRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRoleRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserProfileResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserResponseDTO;
import co.edu.uis.lunchuis.identityservice.application.mapper.UserMapper;
import co.edu.uis.lunchuis.identityservice.application.service.UserManagementService;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.domain.repository.RoleRepository;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of UserManagementService for handling user CRUD operations.
 * Provides methods for user management, profile updates, and password changes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management Service", description = "Service for handling user management operations")
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    // ========== READ OPERATIONS ==========
    
    @Override
    @Operation(summary = "Get all users", description = "Retrieves all users in the system")
    public List<UserResponseDTO> findAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }
    
    @Override
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    public UserResponseDTO findUserById(UUID id) {
        log.info("Retrieving user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Operation(summary = "Get user by institutional code", description = "Retrieves a user by their institutional code")
    public UserResponseDTO findUserByInstitutionalCode(Integer institutionalCode) {
        log.info("Retrieving user with institutional code: {}", institutionalCode);
        User user = userRepository.findByInstitutionalCode(institutionalCode)
                .orElseThrow(() -> new ResourceNotFoundException("User", "institutionalCode", institutionalCode));
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    public UserResponseDTO findUserByEmail(String email) {
        log.info("Retrieving user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Operation(summary = "Get users by role", description = "Retrieves users by their role type")
    public List<UserResponseDTO> findUsersByRole(RoleType roleType) {
        log.info("Retrieving users with role: {}", roleType);
        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleType.name()));
        return userRepository.findByRole(role).stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }
    
    @Override
    @Operation(summary = "Get users by enabled status", description = "Retrieves users by their enabled status")
    public List<UserResponseDTO> findUsersByEnabled(Boolean enabled) {
        log.info("Retrieving users with enabled status: {}", enabled);
        return userRepository.findByEnabled(enabled).stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }
    
    @Override
    @Operation(summary = "Get current user profile", description = "Retrieves the current user's profile information")
    public UserProfileResponse getCurrentUserProfile() {
        String currentUserCode = getCurrentUserInstitutionalCode();
        log.info("Retrieving profile for current user: {}", currentUserCode);
        User user = userRepository.findByInstitutionalCode(Integer.parseInt(currentUserCode))
                .orElseThrow(() -> new ResourceNotFoundException("User", "institutionalCode", currentUserCode));
        return mapToUserProfileResponse(user);
    }
    
    // ========== UPDATE OPERATIONS ==========
    
    @Override
    @Transactional
    @Operation(summary = "Update user", description = "Updates a user's information")
    public UserResponseDTO updateUser(UUID id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }
        
        // Update user information
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setEnabled(request.enabled());
        user.setUpdatedAt(Instant.now());
        
        userRepository.save(user);
        log.info("Successfully updated user with ID: {}", id);
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Transactional
    @Operation(summary = "Update user role", description = "Updates a user's role")
    public UserResponseDTO updateUserRole(UUID id, UpdateUserRoleRequest request) {
        log.info("Updating role for user with ID: {} to role: {}", id, request.roleType());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        Role newRole = roleRepository.findByName(request.roleType())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", request.roleType().name()));
        
        user.setRole(newRole);
        user.setUpdatedAt(Instant.now());
        
        userRepository.save(user);
        log.info("Successfully updated role for user with ID: {}", id);
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Transactional
    @Operation(summary = "Update user status", description = "Updates a user's enabled status")
    public UserResponseDTO updateUserStatus(UUID id, Boolean enabled) {
        log.info("Updating enabled status for user with ID: {} to: {}", id, enabled);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        user.setEnabled(enabled);
        user.setUpdatedAt(Instant.now());
        
        userRepository.save(user);
        log.info("Successfully updated enabled status for user with ID: {}", id);
        return mapToUserResponseDTO(user);
    }
    
    @Override
    @Transactional
    @Operation(summary = "Update profile", description = "Updates the current user's profile information")
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        String currentUserCode = getCurrentUserInstitutionalCode();
        log.info("Updating profile for current user: {}", currentUserCode);
        
        User user = userRepository.findByInstitutionalCode(Integer.parseInt(currentUserCode))
                .orElseThrow(() -> new ResourceNotFoundException("User", "institutionalCode", currentUserCode));
        
        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }
        
        // Update profile information
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUpdatedAt(Instant.now());
        
        userRepository.save(user);
        log.info("Successfully updated profile for current user: {}", currentUserCode);
        return mapToUserProfileResponse(user);
    }
    
    @Override
    @Transactional
    @Operation(summary = "Change password", description = "Changes the current user's password")
    public MessageResponse changePassword(ChangePasswordRequest request) {
        String currentUserCode = getCurrentUserInstitutionalCode();
        log.info("Changing password for current user: {}", currentUserCode);
        
        // Validate password confirmation
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new InvalidRequestException("New password and confirm password do not match");
        }
        
        User user = userRepository.findByInstitutionalCode(Integer.parseInt(currentUserCode))
                .orElseThrow(() -> new ResourceNotFoundException("User", "institutionalCode", currentUserCode));
        
        // Verify current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new UnauthorizedActionException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setUpdatedAt(Instant.now());
        
        userRepository.save(user);
        log.info("Successfully changed password for current user: {}", currentUserCode);
        return new MessageResponse("Password changed successfully");
    }
    
    // ========== DELETE OPERATIONS ==========
    
    @Override
    @Transactional
    @Operation(summary = "Delete user", description = "Deletes a user by their unique identifier")
    public MessageResponse deleteUser(UUID id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
        return new MessageResponse("User deleted successfully");
    }
    
    @Override
    @Transactional
    @Operation(summary = "Delete current user", description = "Deletes the current user's account")
    public MessageResponse deleteCurrentUser() {
        String currentUserCode = getCurrentUserInstitutionalCode();
        log.info("Deleting current user: {}", currentUserCode);
        
        User user = userRepository.findByInstitutionalCode(Integer.parseInt(currentUserCode))
                .orElseThrow(() -> new ResourceNotFoundException("User", "institutionalCode", currentUserCode));
        
        userRepository.delete(user);
        log.info("Successfully deleted current user: {}", currentUserCode);
        return new MessageResponse("Account deleted successfully");
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Maps a User domain object to UserResponseDTO.
     */
    private UserResponseDTO mapToUserResponseDTO(User user) {
        return userMapper.toUserResponseDTO(user);
    }
    
    /**
     * Maps a User domain object to UserProfileResponse.
     */
    private UserProfileResponse mapToUserProfileResponse(User user) {
        return userMapper.toUserProfileResponse(user);
    }
    
    /**
     * Gets the current user's institutional code from the security context.
     */
    private String getCurrentUserInstitutionalCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedActionException("User is not authenticated");
        }
        return authentication.getName();
    }
}
