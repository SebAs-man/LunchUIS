package co.edu.uis.lunchuis.identityservice.infrastructure.security;

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
    private final UserDetailsMapper userDetailsMapper; // Un mapper que crearemos ahora

    /**
     * Loads the user by their email address.
     * @param username The email address of the user to load.
     * @return The {@link UserDetailsImpl} for Spring Security.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByInstitutionalCode(Integer.valueOf(username))
                .map(userDetailsMapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
