package co.edu.uis.lunchuis.common.mapper;

/**
 * A generic base interface for mapping between Domain models and Persistence entities.
 * @param <D> The Domain model type.
 * @param <E> The Entity model type.
 */
public interface BaseEntityMapper<D, E> {
    /**
     * Maps a Persistence entity to a Domain model.
     * @param entity The persistence entity to map.
     * @return The corresponding Domain model.
     */
    D toDomain(E entity);

    /**
     * Maps a Domain model to a Persistence entity.
     * @param domain The domain model to map.
     * @return The corresponding Persistence entity.
     */
    E toEntity(D domain);
}
