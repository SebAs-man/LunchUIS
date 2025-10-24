package co.edu.uis.lunchuis.identityservice.application.mapper;

import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpAdminRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.SignUpRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateProfileRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.request.UpdateUserRequest;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserProfileResponse;
import co.edu.uis.lunchuis.identityservice.application.dto.response.UserResponseDTO;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
     * Maps an {@link SignUpAdminRequest} DTO to a {@link User} domain object.
     * @param request the {@link SignUpAdminRequest} from an admin
     * @return a {@link User} object populated with data from the request
     */
    User toDomain(SignUpAdminRequest request);

    /**
     * Maps a {@link UpdateUserRequest} DTO to a {@link User} domain object.
     * This method is used for updating user information by administrators.
     * @param request the {@link UpdateUserRequest} containing updated user information
     * @return a {@link User} object populated with the updated data
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institutionalCode", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toDomain(UpdateUserRequest request);

    /**
     * Maps a {@link UpdateProfileRequest} DTO to a {@link User} domain object.
     * This method is used for updating user profile information by the user themselves.
     * @param request the {@link UpdateProfileRequest} containing updated profile information
     * @return a {@link User} object populated with the updated profile data
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institutionalCode", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toDomain(UpdateProfileRequest request);

    /**
     * Maps a {@link User} domain object to a {@link UserResponseDTO}.
     * @param user the {@link User} domain object
     * @return a {@link UserResponseDTO} populated with user information
     */
    @Mapping(source = "role.name", target = "role")
    UserResponseDTO toUserResponseDTO(User user);

    /**
     * Maps a {@link User} domain object to a {@link UserProfileResponse}.
     * @param user the {@link User} domain object
     * @return a {@link UserProfileResponse} populated with user profile information
     */
    @Mapping(source = "role.name", target = "role")
    UserProfileResponse toUserProfileResponse(User user);
}
