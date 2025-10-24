package co.edu.uis.lunchuis.comboservice.infrastructure.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Security configuration for the Combo Service.
 * Configures the service as a stateless OAuth2 Resource Server,
 * validating JWTs signed by the Identity Service.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Injects the Base64-encoded secret key from the config-server.
     */
    @Value("${jwt.secret-key}")
    private String jwtSigningKey;

    /**
     * Configures the main security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                // Disable CORS y CSRF
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // Configure session management as stateless (no sessions will be created)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Permit access to Swagger UI and API docs
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/combos/api-docs/**",
                                "/swagger-ui.html",
                                "/actuator/**"
                        ).permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Configure this service as an OAuth2 Resource Server validating JWTs
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                // Use our custom converter to extract roles
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Manually creates the JwtDecoder bean.
     * This replicates the exact Base64 decoding logic from JwtServiceImpl
     * in the identity-server to ensure keys match.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // 1. Decode the Base64 secret key (same as identity-server)
        byte[] keyBytes = jwtSigningKey.getBytes(StandardCharsets.UTF_8);
        // 2. Replicate the exact key generation from identity-server to infer the algorithm
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        // 3. Build the decoder
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    /**
     * Creates a custom converter to extract roles from the JWT 'roles' claim
     * and map them to Spring Security's GrantedAuthority.
     * It adds the "ROLE_" prefix, which @PreAuthorize expects.
     * @return A configured JwtAuthenticationConverter.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Look for the "roles" claim in the JWT
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        // Add "ROLE_" prefix (e.g., "ADMIN" in token becomes "ROLE_ADMIN")
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
