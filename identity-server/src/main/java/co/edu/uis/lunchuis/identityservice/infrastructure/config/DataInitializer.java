package co.edu.uis.lunchuis.identityservice.infrastructure.config;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.RoleEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.UserEntityMapper;
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

/**
 * The {@code DataInitializer} class is responsible for initializing essential
 * data in the system when the application starts. This includes ensuring that
 * all expected roles are present in the database and creating a default admin user
 * if one does not already exist.
 * It implements the {@link CommandLineRunner} interface provided by Spring Boot,
 * which allows code to run after the application context is loaded and the application
 * is fully started.
 * Responsibilities:
 * 1. Ensures that all roles defined in the {@code RoleType} enumeration are present
 *    in the database by checking for missing roles and creating them as needed.
 * 2. Creates a default admin user with predefined credentials and assigns the admin
 *    role to it if the user does not already exist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final JpaRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeAdminUser();
    }

    /**
     * Initializes roles in the system by ensuring that all expected roles,
     * as defined in the {@link RoleType} enumeration, are present in the database.
     * This method checks for missing roles by comparing the roles defined
     * in the {@code RoleType} enum with the roles currently stored in the database.
     * If any expected roles are missing, they are created and saved in bulk operations
     * to maximize efficiency. If all roles are already synchronized, no changes are made.
     * This method is invoked on application startup to ensure the system's role
     * definitions remain consistent with the expected domain model.
     */
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

    /**
     * Initializes the default administrator user in the system if it does not already exist.
     * This method performs the following actions:
     * - Checks if an administrator user exists in the `userRepository` by using a predefined institutional code.
     * - If no administrator is found:
     *   - Searches for the ADMIN role in the `roleRepository` using the role's name.
     *   - Throws an exception if the ADMIN role is not found in the database.
     *   - Creates a default administrator user entity with predefined fields, including:
     *     - A unique identifier (UUID).
     *     - First name and last name set to "Admin" and "User" respectively.
     *     - A default email address and password.
     *     - The ADMIN role fetched from the database.
     *     - Timestamp indicating creation time.
     *   - Saves the new administrator user into the `userRepository`.
     *   - Logs a message confirming the creation of the admin user.
     * - If an administrator user already exists, logs a message indicating no action was taken.
     * The default password for the administrator user is hardcoded and should be changed after creation
     * to maintain system security.
     */
    private void initializeAdminUser() {
        log.info("Checking for admin user initialization...");
        final Integer ADMIN_CODE = 9999999;
        final String ADMIN_PASS = "@dmIn123";

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
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASS));
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
