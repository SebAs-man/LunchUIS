package co.edu.uis.lunchuis.identityservice.application.service;

import co.edu.uis.lunchuis.common.dto.MessageResponse;
import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.common.exception.DuplicateResourceException;
import co.edu.uis.lunchuis.common.exception.ResourceNotFoundException;
import co.edu.uis.lunchuis.identityservice.application.dto.request.LoginRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.JwtAuthenticationResponse;
import co.edu.uis.lunchuis.identityservice.application.mapper.UserMapper;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.domain.repository.RoleRepository;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
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
    public JwtAuthenticationResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Login functionality not implemented yet.");
    }
}
