package co.edu.uis.lunchuis.identityservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for testing security configuration and authentication functionality.
 * This controller provides endpoints designed exclusively for development and testing purposes.
 * It should NOT be deployed to production environments.
 * The endpoints help verify:
 *   <li>Security filter chain is working correctly</li>
 *   <li>JWT authentication is properly configured</li>
 *   <li>Protected vs. public endpoints are correctly distinguished</li>
 *   <li>User authentication context is accessible</li>
 */
@RestController
@Tag(
        name = "Testing & Diagnostics",
        description = "Endpoints for testing security configuration and authentication. " +
                "⚠️ FOR DEVELOPMENT/TESTING ONLY - Remove before production deployment"
)
class TestController {

    /**
     * Tests if the security configuration is properly protecting endpoints.
     * This endpoint is intentionally secured and should only be accessible with a valid JWT token.
     * If you can access this endpoint without authentication, it indicates a security misconfiguration.
     * @return A message indicating successful authentication
     */
    @Operation(
            summary = "Test secured endpoint",
            description = "Verifies that the security filter chain is properly protecting endpoints. " +
                    "This endpoint requires a valid JWT token in the Authorization header. " +
                    "Use this to test if your authentication configuration is working correctly.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated and authorized",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Security test passed! You are authenticated."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Unauthorized\", \"message\": \"Full authentication is required\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Valid token but insufficient permissions",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Forbidden\", \"message\": \"Access Denied\" }"
                            )
                    )
            )
    })
    @GetMapping("/api/v1/test")
    public String getSecuredData() {
        return "¡Si ves esto, la seguridad falló!";
    }


    /**
     * Retrieves the authenticated user's information from the security context.
     * This endpoint demonstrates how to access the currently authenticated user's details
     * from Spring Security's SecurityContextHolder.
     * Security Note: This endpoint requires a valid JWT token. The token's subject
     * (usually the institutional code or username) will be returned in the response.
     * @return ResponseEntity containing a personalized greeting with the authenticated user's identifier
     */
    @Operation(
            summary = "Get authenticated user information",
            description = "Returns a greeting message with the currently authenticated user's identifier. " +
                    "This endpoint extracts the authentication principal from the security context, " +
                    "which is populated by the JWT authentication filter. " +
                    "Use this to verify that JWT parsing and user context setting are working correctly.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved authenticated user information",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Hello from a secured endpoint! You are authenticated as: 123456"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Unauthorized\", \"message\": \"Full authentication is required\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error - Authentication context is null (should not happen if authenticated)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Internal Server Error\", \"message\": \"Authentication context is null\" }"
                            )
                    )
            )
    })
    @GetMapping("api/auth/hello")
    public ResponseEntity<String> sayHello() {
        // Get the authenticated user's details from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return ResponseEntity.ok("Hello from a secured endpoint! You are authenticated as: " + currentPrincipalName);
    }
}