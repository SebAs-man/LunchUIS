package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper;

import co.edu.uis.lunchuis.common.mapper.BaseEntityMapper;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain model {@link Role} and persistence entity {@link RoleEntity}.
 * This mapper facilitates conversions necessary for bridging the domain and
 * persistence layers, ensuring consistency and separation of concerns.
 */
@Mapper(componentModel = "spring")
public interface RoleEntityMapper extends BaseEntityMapper<Role, RoleEntity> {
}
