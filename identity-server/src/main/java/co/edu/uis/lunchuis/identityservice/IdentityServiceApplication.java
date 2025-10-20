package co.edu.uis.lunchuis.identityservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Identity Service application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "co.edu.uis.lunchuis.identityservice",
        "co.edu.uis.lunchuis.common"
})
@OpenAPIDefinition(info = @Info(title = "Identity Service API", version = "1.0",
        description = "API for managing users and authentication"))
public class IdentityServiceApplication
{
    public static void main( String[] args ){
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
