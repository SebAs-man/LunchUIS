package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link RoleEntity} persistence.
 * Provides CRUD operations and custom query methods using Spring Data JPA.
 */
@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, UUID> {
    /**
     * Finds a role by its name.
     * @param name The name of the role (Enum).
     * @return An {@link Optional} containing the found role or empty if not found.
     */
    Optional<RoleEntity> findByName(RoleType name);
}
