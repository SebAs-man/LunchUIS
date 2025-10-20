package co.edu.uis.lunchuis.identityservice.domain.repository;

import co.edu.uis.lunchuis.identityservice.domain.entity.Role;
import co.edu.uis.lunchuis.identityservice.domain.valueobject.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Role} entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    /**
     * Finds a role by its name.
     * @param name The name of the role (Enum).
     * @return An {@link Optional} containing the found role or empty if not found.
     */
    Optional<Role> findByName(RoleType name);
}
