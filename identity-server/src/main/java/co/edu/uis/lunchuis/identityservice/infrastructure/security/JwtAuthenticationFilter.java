package co.edu.uis.lunchuis.identityservice.infrastructure.security;

import co.edu.uis.lunchuis.identityservice.application.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JWT Authentication Filter for validating requests.
 * This filter intercepts every HTTP request and checks for the presence of a JWT in the
 * Authorization header ("Bearer <token>"). If a valid JWT is present, it loads the
 * user details using {@link UserDetailsService} and sets the authentication in the
 * Spring Security context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        // 1. Check if the Authorization header is present and correctly formatted
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // If not, pass the request to the next filter
            return;
        }
        // 2. Extract the token from the header
        final String jwt = authHeader.substring(7); // "Bearer ".length()
        final String userCode = jwtService.extractUsername(jwt);
        // 3. Validate the token
        // If we have a user email, and they are not yet authenticated
        if (userCode != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userCode);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // If the token is valid, create an Authentication object and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't need credentials
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 4. Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
