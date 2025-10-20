package co.edu.uis.lunchuis.identityservice.application.service.impl;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.common.exception.DuplicateResourceException;
import co.edu.uis.lunchuis.common.exception.ResourceNotFoundException;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Tag(name = "Authentication Service", description = "Service for handling user authentication operations")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    @Override
    @Transactional
    @Operation(summary = "Register a new user", description = "Registers a new user with the STUDENT role and stores it in the database")
    public MessageResponse signup(SignUpRequest request) {
        // 1. Check if a user already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }
        if(userRepository.existsByInstitutionalCode(request.institutionalCode())){
            throw new DuplicateResourceException("User", "institutionalCode", request.institutionalCode());
        }
        // 2. Find the default role for a new user (STUDENT)
        Role userRole = roleRepository.findByName(RoleType.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.STUDENT.name()));
        // 3. Create a new user entity from the request DTO
        User user = userMapper.toDomain(request);
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4. Save the new user to the database
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

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
}
