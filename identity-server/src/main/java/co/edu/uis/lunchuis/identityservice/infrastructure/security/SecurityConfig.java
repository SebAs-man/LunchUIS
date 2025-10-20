package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class for defining beans related to security.
 */
@Configuration
public class SecurityConfig {
    /**
     * Provides a PasswordEncoder bean to the Spring application context.
     * This bean will be used for hashing and verifying passwords.
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
