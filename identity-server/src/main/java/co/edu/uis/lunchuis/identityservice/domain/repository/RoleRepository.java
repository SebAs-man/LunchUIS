package co.edu.uis.lunchuis.identityservice.domain.repository;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;

import java.util.Optional;

/**
 * Repository interface for managing {@link Role} aggregates within the domain layer.
 * This interface defines the contract for retrieving role entities
 * from the underlying data source, abstracting persistence concerns from
 * the domain logic.
 */
public interface RoleRepository {
    /**
     * Retrieves a {@link Role} by its specific {@link RoleType}.
     * The role type typically corresponds to predefined system roles such as
     * {@code ADMIN} or {@code STUDENT}.
     * @param name the role type to search for (must not be {@code null})
     * @return an {@link Optional} containing the role if found, or empty otherwise
     */
    Optional<Role> findByName(RoleType name);
}
