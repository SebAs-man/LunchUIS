package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import co.edu.uis.lunchuis.common.exception.InvalidRequestException;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService}.
 * This service is responsible for loading a user's details from the
 * database given their "username" (which, in this application, is their email).
 * It integrates with the {@link UserRepository} and maps the domain
 * model to the security model ({@link UserDetailsImpl}).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    /**
     * Loads a user by their institutional code.
     * This is used by Spring Security during authentication and for JWT validation.
     * @param username The institutional code as a string (Spring Security uses the "username" concept)
     * @return The {@link UserDetailsImpl} for Spring Security
     * @throws UsernameNotFoundException if no user is found with the given institutional code
     */
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            int code = Integer.parseInt(username);;
            return userRepository.findByInstitutionalCode(code)
                    .map(userDetailsMapper::toUserDetails)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with institutional code: " + username)
                    );
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("Invalid institutional code: " + username);
        }
    }
}
