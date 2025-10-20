package co.edu.uis.lunchuis.identityservice.domain.repository;

import co.edu.uis.lunchuis.identityservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by their institutional email.
     * @param email The user's email.
     * @return An {@link Optional} containing the found user or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their institutional code.
     * @param institutionalCode The user's institutional code.
     * @return An {@link Optional} containing the found user or empty if not found.
     */
    Optional<User> findByInstitutionalCode(Integer institutionalCode);

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
