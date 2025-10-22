package co.edu.uis.lunchuis.identityservice.application.mapper;

import co.edu.uis.lunchuis.identityservice.application.dto.request.AdminUserCreationRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import org.mapstruct.Mapper;

/**
 * Maps between DTOs and domain objects for the User aggregate.
 * This mapper operates at the application layer and must not depend on
 * any persistence or framework-specific classes (e.g., JPA entities).
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a {@link SignUpRequest} DTO to a {@link User} domain object.
     * @param request the {@link SignUpRequest} containing user registration details
     * @return a {@link User} object populated with the corresponding data from the request
     */
    User toDomain(SignUpRequest request);

    /**
     * Maps an {@link AdminUserCreationRequest} DTO to a {@link User} domain object.
     * @param request the {@link AdminUserCreationRequest} from an admin
     * @return a {@link User} object populated with data from the request
     */
    User toDomain(AdminUserCreationRequest request);
}
