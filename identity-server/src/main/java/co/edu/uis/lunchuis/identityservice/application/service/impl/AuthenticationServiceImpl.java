package co.edu.uis.lunchuis.identityservice.application.service.impl;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.common.exception.DuplicateResourceException;
import co.edu.uis.lunchuis.common.exception.ResourceNotFoundException;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpAdminRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.LoginRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.JwtAuthenticationResponse;
import co.edu.uis.lunchuis.identityservice.application.mapper.UserMapper;
import co.edu.uis.lunchuis.identityservice.application.service.AuthenticationService;
import co.edu.uis.lunchuis.identityservice.application.service.JwtService;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.domain.repository.RoleRepository;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class provides the implementation of the {@link AuthenticationService} interface.
 * It handles operations related to user authentication such as user registration,
 * authentication, and administrative user creation.
 * The service interacts with the database repositories for users and roles, encodes
 * passwords securely, and generates JWT tokens for authenticated users. It uses
 * transactional operations to ensure data consistency and includes validation to
 * prevent duplicate user entries.
 */
@Service
@RequiredArgsConstructor
@Tag(name = "Authentication Service",
        description = "Service for handling user authentication operations")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    /**
     * Registers a new user with the STUDENT role and stores the user in the database.
     * This method ensures the provided email and institutional code are unique.
     * @param request the SignUpRequest object containing the new user's details such as
     *                email, password, name, and institutional code.
     * @return a MessageResponse indicating the successful registration of the user.
     * @throws DuplicateResourceException if a user already exists with the provided email
     *                                    or institutional code.
     * @throws ResourceNotFoundException if the STUDENT role is not found in the database.
     */
    @Override
    @Transactional
    @Operation(summary = "Register a new user",
            description = "Registers a new user with the STUDENT role and stores it in the database")
    public MessageResponse signup(SignUpRequest request) {
        // 1. Check if a user already exists & Find the default role for a new user (STUDENT)
        Role userRole = this.verifyRole(request.email(), request.institutionalCode(), RoleType.STUDENT);
        // 2. Create a new user entity from the request DTO
        User user = userMapper.toDomain(request);
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Save the new user to the database
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    /**
     * Authenticates a user by institutional code and password and generates a JWT token
     * upon successful authentication. This method uses Spring Security's
     * AuthenticationManager and interacts with the user repository to fetch the user's
     * details and generate a token.
     * @param request an instance of {@link LoginRequest} containing the institutional code
     *                and password of the user attempting to authenticate.
     * @return a {@link JwtAuthenticationResponse} containing the generated JWT token for the
     *         authenticated user.
     * @throws ResourceNotFoundException if the user with the specified institutional code
     *                                    does not exist in the database.
     */
    @Override
    @Operation(summary = "Authenticate a user",
            description = "Authenticates a user by institutional code and password, returning a JWT token"
    )
    public JwtAuthenticationResponse login(LoginRequest request) {
        // 1. Authenticate the user using Spring Security's AuthenticationManager
        // This will trigger our UserDetailsServiceImpl and use the PasswordEncoder
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.institutionalCode(), request.password())
        );
        // 2. If authentication is successful, find the user from our database
        var user = userRepository.findByInstitutionalCode(request.institutionalCode())
                .orElseThrow(() -> new ResourceNotFoundException("User", "code", request.institutionalCode()));
        // 3. Generate a JWT for the authenticated user
        var jwt = jwtService.generateToken(user);
        // 4. Return the token in the response DTO
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Creates a new user with a specified role. This method is intended for use by administrators.
     * It verifies the uniqueness of the user's email and institutional code, assigns the specified
     * role to the user, securely encrypts their password, and saves the user to the database.
     * @param request an instance of {@link SignUpAdminRequest} containing the details
     *                for the new admin-created user, such as email, institutional code,
     *                role type, and password.
     * @return a {@link MessageResponse} indicating successful creation of the user by the admin.
     * @throws DuplicateResourceException if a user with the provided email or institutional code
     *                                    already exists.
     * @throws ResourceNotFoundException if the specified role does not exist in the database.
     */
    @Override
    @Transactional
    @Operation(summary = "Create a new user by admin",
            description = "Creates a new user with a specified role, intended for administrative use.")
    public MessageResponse signupadmin(SignUpAdminRequest request) {
        // 1. Check if a user already exists & Find the specified role
        Role userRole = this.verifyRole(request.email(), request.institutionalCode(), request.roleType());

        // 2. Create a new user entity from the request DTO
        User user = userMapper.toDomain(request);
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Save the new user to the database
        userRepository.save(user);

        return new MessageResponse("User created successfully by admin!");
    }

    /**
     * Verifies if a role exists in the system and checks for uniqueness of the user's email
     * and institutional code. If the email or code already exists, an exception is thrown.
     * If the specified role type does not exist, an exception is also thrown.
     * @param email the email address of the user to be verified
     * @param code the institutional code of the user to be verified
     * @param roleType the type of role to be retrieved
     * @return the {@link Role} associated with the specified role type
     * @throws DuplicateResourceException if a user with the provided email or institutional code already exists
     * @throws ResourceNotFoundException if the specified role type is not found in the system
     */
    private Role verifyRole(String email, Integer code, RoleType roleType){
        // 1. Check if a user already exists with the given email or institutional code
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("User", "email", email);
        }
        if (userRepository.existsByInstitutionalCode(code)) {
            throw new DuplicateResourceException("User", "institutionalCode", code);
        }
        // 2. Find the specified role
        return roleRepository.findByName(roleType).orElse(null);
    }
}
