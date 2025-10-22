package co.edu.uis.lunchuis.identityservice.infrastructure.config;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.RoleEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Initializes the role data in the database during application startup.
 * Implements the {@link CommandLineRunner} to execute the initialization logic
 * after the application context is loaded.
 * The logic ensures that all roles defined in the {@link RoleType} enumeration
 * are present in the database. If any roles are missing, they are created and
 * saved in a batch query. If all roles already exist, a log message indicates
 * synchronization is complete.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final JpaRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;

    /**
     * Executes the logic to initialize and synchronize roles in the database
     * based on the predefined {@link RoleType} enumeration. Missing roles are
     * identified and created in a single batch operation, ensuring that all
     * roles defined in the system are present in the database. Logs the process
     * and completion status.
     * @param args the command-line arguments passed to the application during startup (not used in this implementation)
     */
    @Override
    public void run(String... args) {
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
}
