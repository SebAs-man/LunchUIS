package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import co.edu.uis.lunchuis.identityservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation for Spring Security.
 * This class acts as an Adapter between the application's domain
 * {@link User} model and the {@link UserDetails} interface required
 * by Spring Security.
 */
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final User user;

    /**
     * Returns the authorities (roles) granted to the user.
     * We map our domain Role to Spring's SimpleGrantedAuthority.
     * The role name must be prefixed with "ROLE_" for hasRole() expressions to work.
     * @return A collection containing the user's role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(
                "ROLE_" + user.getRole().name().name())
        );
    }

    // --- Getters ---

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getInstitutionalCode());
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
