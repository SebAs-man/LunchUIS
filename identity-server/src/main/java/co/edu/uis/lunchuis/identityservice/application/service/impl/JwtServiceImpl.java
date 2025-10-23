package co.edu.uis.lunchuis.identityservice.application.service.impl;

import co.edu.uis.lunchuis.identityservice.application.service.JwtService;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Service implementation for handling JSON Web Tokens (JWT).
 * Provides methods for generating, validating, and extracting information from JWT tokens.
 */
@Service
@Tag(name = "JWT Service", description = "Service for handling JWT token operations")
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Extracts the username embedded in the provided JWT token.
     * @param token the JWT token from which the username is to be extracted
     * @return the username (institutional code) extracted from the token
     */
    @Override
    @Operation(summary = "Extract username from JWT",
            description = "Extracts the institutional code from the given JWT token")
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    @Operation(summary = "Generate JWT token",
            description = "Generates a JWT token for the specified user")
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getInstitutionalCode()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    @Operation(summary = "Validate JWT token",
            description = "Checks if the JWT token is valid and belongs to the given user")
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token has expired.
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     * @param <T>            the type of the claim
     * @param token          the JWT token
     * @param claimsResolver function to extract the specific claim from Claims
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims (payload) from the JWT token.
     * @param token the JWT token
     * @return all claims contained in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Returns the secret key used to sign the JWT.
     * @return the secret key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
