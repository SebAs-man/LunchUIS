package co.edu.uis.lunchuis.identityservice.infrastructure.config;

import co.edu.uis.lunchuis.identityservice.domain.entity.Role;
import co.edu.uis.lunchuis.identityservice.domain.repository.RoleRepository;
import co.edu.uis.lunchuis.identityservice.domain.valueobject.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking for roles initialization...");
        if (roleRepository.findByName(RoleType.STUDENT).isEmpty()) {
            log.info("Creating ROLE_STUDENT...");
            roleRepository.save(Role.builder().name(RoleType.STUDENT).build());
        }
        if (roleRepository.findByName(RoleType.ADMIN).isEmpty()) {
            log.info("Creating ROLE_ADMIN...");
            roleRepository.save(Role.builder().name(RoleType.ADMIN).build());
        }
        log.info("Roles initialization check complete.");
    }
}
