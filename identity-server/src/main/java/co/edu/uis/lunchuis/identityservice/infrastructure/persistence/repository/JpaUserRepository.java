package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository;

import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link UserEntity} persistence.
 * Provides CRUD operations and custom query methods using Spring Data JPA.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    /**
     * Finds a user by their institutional email.
     * @param email The user's email.
     * @return An {@link Optional} containing the found user or empty if not found.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a user by their institutional code.
     * @param institutionalCode The user's institutional code.
     * @return An {@link Optional} containing the found user or empty if not found.
     */
    Optional<UserEntity> findByInstitutionalCode(Integer institutionalCode);

    /**
     * Finds users by their role.
     * @param role The role to search for.
     * @return A list of users with the specified role.
     */
    List<UserEntity> findByRole(RoleEntity role);

    /**
     * Finds users by their enabled status.
     * @param enabled The enabled status to search for.
     * @return A list of users with the specified enabled status.
     */
    List<UserEntity> findByEnabled(Boolean enabled);

    /**
     * Checks if a user exists with the given email.
     * @param email The email to check.
     * @return true if a user exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Checks if a user exists with the given institutional code.
     * @param institutionalCode The code to check.
     * @return true if a user exists, false otherwise.
     */
    Boolean existsByInstitutionalCode(Integer institutionalCode);
}
