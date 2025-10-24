package co.edu.uis.lunchuis.comboservice.infrastructure.persistence.repository;

import co.edu.uis.lunchuis.comboservice.infrastructure.persistence.entity.ComboEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link ComboEntity}.
 * This interface is used by the persistence adapter.
 */
@Repository
public interface JpaComboRepository extends JpaRepository<ComboEntity, UUID> {
    /**
     * Finds a combo by its unique name.
     * @param name The name of the combo (case-sensitive).
     * @return an Optional containing the ComboEntity if found.
     */
    Optional<ComboEntity> findByName(String name);
}
