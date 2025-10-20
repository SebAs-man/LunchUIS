package co.edu.uis.lunchuis.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standardized response DTO for simple success messages.
 * Used for operations like DELETE or actions that trigger
 * an asynchronous process.
 * @param message The success message to be returned to the client.
 * @author CodingPartner
 */
@Schema(name = "MessageResponse", description = "Standard response for simple success messages")
public record MessageResponse(
        @Schema(description = "A human-readable success message",
                example = "Combo 'COMBO_01' deleted successfully.")
        String message
) {
}
