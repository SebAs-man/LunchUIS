package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper;

import co.edu.uis.lunchuis.common.mapper.BaseEntityMapper;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * Maps between domain model {@link User} and persistence entity {@link UserEntity}.
 * Used internally by repository adapters in the infrastructure layer.
 */
@Mapper(componentModel = "spring")
public interface UserEntityMapper extends BaseEntityMapper<User, UserEntity> {
}
