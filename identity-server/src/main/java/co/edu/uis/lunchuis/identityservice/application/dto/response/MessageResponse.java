package co.edu.uis.lunchuis.identityservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Represents a generic response containing a message.
 * This DTO is used to communicate simple textual responses back to the client.
 * It provides a standardized way of encapsulating response messages in the application.
 */
@Value
@AllArgsConstructor
@Schema(description = "A generic response DTO for messages.")
public class MessageResponse {
    @Schema(description = "The response message.",
            example = "Operation was successful.")
    String message;
}
