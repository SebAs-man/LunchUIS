package co.edu.uis.lunchuis.comboservice.infrastructure.persistence.mapper;

import co.edu.uis.lunchuis.comboservice.domain.model.Combo;
import co.edu.uis.lunchuis.comboservice.infrastructure.persistence.entity.ComboEntity;
import co.edu.uis.lunchuis.common.mapper.BaseEntityMapper;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between {@link Combo} domain model
 * and {@link ComboEntity} persistence entity.
 * This uses MapStruct and inherits from the common BaseEntityMapper.
 */
@Mapper(componentModel = "spring")
public interface ComboEntityMapper extends BaseEntityMapper<Combo, ComboEntity> {
    /**
     * Converts a list of {@link ComboEntity} instances to a list of {@link Combo} domain objects.
     * @param entities the list of {@link ComboEntity} objects to be converted; should not be null.
     * @return a list of converted {@link Combo} domain objects.
     */
    List<Combo> toDomainList(List<ComboEntity> entities);

    /**
     * Converts a list of {@link Combo} domain objects into a list of {@link ComboEntity} persistence entities.
     * @param combos the list of {@link Combo} objects to be converted; must not be null.
     * @return a list of {@link ComboEntity} instances corresponding to the provided {@link Combo} objects.
     */
    List<ComboEntity> toEntityList(List<Combo> combos);
}
