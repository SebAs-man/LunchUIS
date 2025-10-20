package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import co.edu.uis.lunchuis.identityservice.domain.model.User;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper to convert a domain {@link User} object
 * into a Spring Security {@link UserDetailsImpl} object.
 */
@Mapper(componentModel = "spring")
public interface UserDetailsMapper {
    /**
     * Maps a User domain model to its UserDetails representation.
     * @param user The domain user object.
     * @return The corresponding UserDetailsImpl object.
     */
    UserDetailsImpl toUserDetails(User user);
}
