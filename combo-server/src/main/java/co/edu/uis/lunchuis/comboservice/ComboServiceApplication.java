package co.edu.uis.lunchuis.comboservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Combo Service application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "co.edu.uis.lunchuis.comboservice",
        "co.edu.uis.lunchuis.common"
})
@OpenAPIDefinition(info = @Info(title = "Combo Service API", version = "1.0",
        description = "API for managing food combos."))
public class ComboServiceApplication
{
    public static void main( String[] args ) {
        SpringApplication.run(ComboServiceApplication.class, args);
    }
}
