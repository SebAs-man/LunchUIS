package co.edu.uis.lunchuis.identityservice.infrastructure.config;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final JpaRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking for roles initialization...");
        // 1. Get all roles that *should* exist from the enum
        Set<RoleType> expectedRoles = Arrays.stream(RoleType.values())
                .collect(Collectors.toSet());
        // 2. Get all roles that *currently* exist in the database in ONE query
        Set<RoleType> existingRoles = roleRepository.findAll().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
        // 3. Determine which roles are missing
        List<RoleEntity> newRoles = expectedRoles.stream()
                .filter(expectedRole -> !existingRoles.contains(expectedRole))
                .map(roleType -> {
                    log.info("Role '{}' not found in database. Creating it...", roleType);
                    return RoleEntity.builder().name(roleType).build();
                })
                .toList();

        // 4. Save all missing roles in ONE batch query
        if (!newRoles.isEmpty()) {
            roleRepository.saveAll(newRoles);
            log.info("Successfully created {} new role(s).", newRoles.size());
        } else {
            log.info("All roles are already synchronized with the database.");
        }
        log.info("Roles initialization complete.");
    }
}
