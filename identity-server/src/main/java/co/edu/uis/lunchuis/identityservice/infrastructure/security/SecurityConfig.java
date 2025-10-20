package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Central configuration class for Spring Security in the Identity Service.
 * This class enables web security and defines all the necessary beans for
 * authentication and authorization. It is configured specifically for a
 * stateless (JWT-based) REST API, disabling CSRF and session management.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Custom service for loading user-specific data.
     * Injected via constructor.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Configures the main security filter chain for the application.
     * This bean defines the security rules for all HTTP requests, including
     * 1. Disabling CSRF protection (suitable for stateless REST APIs).
     * 2. Defining authorization rules (which endpoints are public, which are protected).
     * 3. Setting session management to STATELESS (no sessions are created).
     * 4. Registering the custom {@link AuthenticationProvider}.
     * @param http The {@link HttpSecurity} builder to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF, as we are using JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // 2. Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/**", // login/registration endpoints
                                "/swagger-ui/**", // Swagger JS and CSS
                                "/v3/api-docs/**", // El JSON de la API
                                "/swagger-ui.html"
                        ).permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                // 3. Set session management to STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // En el futuro, aquí se añadirá el filtro de validación de JWT

        return http.build();
    }

    /**
     * Exposes the {@link AuthenticationManager} as a bean.
     * The AuthenticationManager is the main interface for authentication.
     * It will be used by our authentication service (e.g., in the login endpoint)
     * to process authentication requests using the configured provider.
     * @param config The global {@link AuthenticationConfiguration}.
     * @return The {@link AuthenticationManager} bean.
     * @throws Exception if an error occurs retrieving the manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides the {@link PasswordEncoder} bean to the Spring application context.
     * This bean is used for hashing passwords before saving them to the
     * database and for verifying passwords during authentication.
     * We use {@link BCryptPasswordEncoder} as it is the industry standard
     * for secure password hashing.
     * @return An instance of {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
