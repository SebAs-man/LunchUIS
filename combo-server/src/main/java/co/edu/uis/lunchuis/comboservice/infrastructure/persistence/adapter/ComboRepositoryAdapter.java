package co.edu.uis.lunchuis.comboservice.infrastructure.persistence.adapter;

import co.edu.uis.lunchuis.comboservice.domain.model.Combo;
import co.edu.uis.lunchuis.comboservice.domain.repository.ComboRepository;
import co.edu.uis.lunchuis.comboservice.infrastructure.persistence.entity.ComboEntity;
import co.edu.uis.lunchuis.comboservice.infrastructure.persistence.mapper.ComboEntityMapper;
import co.edu.uis.lunchuis.comboservice.infrastructure.persistence.repository.JpaComboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence Adapter that implements the {@link ComboRepository} port.
 * This class bridges the domain layer with the persistence (JPA) layer.
 */
@Repository
@RequiredArgsConstructor
public class ComboRepositoryAdapter implements ComboRepository {
    private final JpaComboRepository repository;
    private final ComboEntityMapper mapper;

    @Override
    public Combo save(Combo combo) {
        ComboEntity comboEntity = mapper.toEntity(combo);
        ComboEntity comboSaved = repository.save(comboEntity);
        return mapper.toDomain(comboSaved);
    }

    @Override
    public Optional<Combo> findById(UUID id) {
        return repository.findById(id)
                .map( mapper::toDomain);
    }

    @Override
    public Optional<Combo> findByName(String name) {
        return repository.findByName(name)
                .map( mapper::toDomain );
    }

    @Override
    public List<Combo> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public List<Combo> findBetweenDates(LocalDate from, LocalDate to) {
        return List.of();
    }

    @Override
    public Combo findByDate(LocalDate date) {
        return null;
    }

    @Override
    public boolean existsByDate(LocalDate date) {
        return false;
    }
}
