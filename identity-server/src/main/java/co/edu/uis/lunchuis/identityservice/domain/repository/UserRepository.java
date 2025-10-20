package co.edu.uis.lunchuis.identityservice.domain.repository;

import co.edu.uis.lunchuis.identityservice.domain.model.User;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} aggregate roots within the domain layer.
 * This interface defines the contract for accessing and persisting user entities,
 * abstracting away the details of the underlying data source.
 * Implementations of this interface (for example, JPA-based or in-memory repositories)
 * should reside in the infrastructure layer.
 */
public interface UserRepository {
    /**
     * Persists a {@link User} instance in the underlying data store.
     * Depending on the implementation, this method may either insert or update
     * the given user.
     *
     * @param user the user aggregate to persist (must not be {@code null})
     */
    void save(User user);

    /**
     * Retrieves a {@link User} by its unique institutional email address.
     * @param email the email address to search for (must not be {@code null})
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a {@link User} by its institutional code.
     * @param code the institutional code to search for (must not be {@code null})
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByInstitutionalCode(Integer code);

    /**
     * Checks whether a {@link User} with the given email already exists.
     * @param email the email address to check (must not be {@code null})
     * @return {@code true} if a user exists with the given email, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks whether a {@link User} with the given institutional code already exists.
     * @param code the institutional code to check (must not be {@code null})
     * @return {@code true} if a user exists with the given institutional code, {@code false} otherwise
     */
    boolean existsByInstitutionalCode(Integer code);
}
