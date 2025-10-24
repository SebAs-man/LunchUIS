package co.edu.uis.lunchuis.comboservice.domain.repository;

import co.edu.uis.lunchuis.comboservice.domain.model.Combo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Defines a repository interface for managing Combo entities.
 * Provides methods to perform standard CRUD operations and advanced queries
 * such as searching by name, date, or date ranges.
 */
public interface ComboRepository {
    /**
     * Persists the specified Combo entity in the repository. This method can be
     * used to save a new Combo or update an existing one if it already exists.
     * @param combo the Combo entity to be saved. Must not be null and should
     *              contain valid details such as name, description, price, and
     *              other required fields.
     */
    void save(Combo combo);

    /**
     * Retrieves a Combo entity by its unique identifier.
     * This method searches the repository for a Combo with the specified UUID
     * and returns it as an Optional if found. If no Combo is found, the Optional
     * will be empty.
     * @param id the unique identifier of the Combo to be retrieved. Must not be null.
     * @return an Optional containing the Combo if found, or an empty Optional if no Combo exists with the given ID.
     */
    Optional<Combo> findById(UUID id);

    /**
     * Searches for a Combo entity by its name.
     * This method retrieves a Combo based on the specified name from the repository.
     * If a Combo with the given name exists, it is returned wrapped in an Optional.
     * Otherwise, the Optional will be empty.
     * @param name the name of the Combo to be searched. Must not be null.
     * @return an Optional containing the Combo if found, or an empty Optional if no Combo exists with the given name.
     */
    Optional<Combo> findByName(String name);

    /**
     * Retrieves all Combo entities from the repository.
     * This method returns a list containing all Combo objects currently managed
     * by the repository. If no Combo entities are present, an empty list is returned.
     * @return a list of all Combo entities, or an empty list if no combos exist.
     */
    List<Combo> findAll();

    /**
     * Deletes the Combo entity associated with the specified unique identifier.
     * This method removes a Combo from the repository if it exists for the provided UUID.
     * If no Combo with the given ID exists, no action is performed.
     * @param id the unique identifier of the Combo to be deleted. Must not be null.
     */
    void deleteById(UUID id);

    /**
     * Checks if an entity with the specified unique identifier exists in the repository.
     * @param id the unique identifier of the entity to check for existence. Must not be null.
     * @return true if an entity with the given identifier exists, false otherwise.
     */
    boolean existById(UUID id);

    /**
     * Retrieves a list of Combo entities that are valid within the specified date range.
     * @param from the start date of the range (inclusive). Must not be null.
     * @param to the end date of the range (inclusive). Must not be null.
     * @return a list of Combo entities that have a valid date range overlapping with the specified dates.
     */
    List<Combo> findBetweenDates(LocalDate from, LocalDate to);

    /**
     * Retrieves a Combo entity that is valid for the specified date.
     * This method searches the repository for a Combo whose validity range
     * includes the specified date and returns it if found.
     * @param date the date for which the Combo is to be retrieved. Must not be null.
     * @return the Combo entity that matches the specified date or null if no such Combo exists.
     */
    Combo findByDate(LocalDate date);

    /**
     * Checks if a Combo entity exists for the specified date.
     * @param date the date for which the existence of a Combo entity is to be checked. Must not be null.
     * @return true if a Combo entity exists for the given date, false otherwise.
     */
    boolean existsByDate(LocalDate date);
}
