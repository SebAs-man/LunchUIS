package co.edu.uis.lunchuis.identityservice.infrastructure.config;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.RoleEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaRoleRepository;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final JpaRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        log.info("Checking for roles initialization...");
        // 1. Get all roles that *should* exist from the enum
        Set<RoleType> expectedRoles = Arrays.stream(RoleType.values())
                .collect(Collectors.toSet());
        // 2. Get all roles that *currently* exist in the database in ONE query
        Set<RoleType> existingRoles = roleRepository.findAll().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
        // 3. Determine which roles are missing
        List<Role> newRoles = expectedRoles.stream()
                .filter(expectedRole -> !existingRoles.contains(expectedRole))
                .map(roleType -> {
                    log.info("Role '{}' not found in database. Creating it...", roleType);
                    return new Role(null, roleType);
                })
                .toList();

        // 4. Save all missing roles in ONE batch query
        if (!newRoles.isEmpty()) {
            List<RoleEntity> newEntities = newRoles.stream()
                    .map(roleEntityMapper::toEntity)
                    .toList();
            roleRepository.saveAll(newEntities);
            log.info("Successfully created {} new role(s).", newRoles.size());
        } else {
            log.info("All roles are already synchronized with the database.");
        }
        log.info("Roles initialization complete.");
    }

    private void initializeAdminUser() {
        log.info("Checking for admin user initialization...");
        final Integer ADMIN_CODE = 999999;

        if (!userRepository.existsByInstitutionalCode(ADMIN_CODE)) {
            log.info("Admin user not found. Creating default admin user...");

            RoleEntity adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new IllegalStateException("FATAL: ADMIN role not found. Cannot create admin user."));

            UserEntity adminUser = new UserEntity();
            adminUser.setId(UUID.randomUUID());
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setInstitutionalCode(ADMIN_CODE);
            adminUser.setEmail("admin@lunchuis.com");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // The default password should be changed
            adminUser.setRole(adminRole);
            adminUser.setEnabled(true);
            adminUser.setCreatedAt(Instant.now());

            userRepository.save(adminUser);
            log.info("Default admin user created successfully with institutional code: {}", ADMIN_CODE);
        } else {
            log.info("Admin user already exists. Skipping creation.");
        }
        log.info("Admin user initialization complete.");
    }
}
