package co.edu.uis.lunchuis.comboservice.application.dto.request;

import co.edu.uis.lunchuis.common.enums.ComboStatus;
import co.edu.uis.lunchuis.common.enums.ComboType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for handling the creation or update of a Combo offering.
 * Represents details required to define a combo offering, including its attributes
 * such as name, description, price, status, type, quota, and validity dates.
 * This DTO is primarily used in requests for operations related to combos
 * in the system, ensuring validation rules are adhered to and a proper structure is maintained.
 * Fields:
 * - name: Name of the combo with validation for presence and character length.
 * - description: Optional detailed description of the combo, with a maximum character limit.
 * - price: The cost of the combo, which must be a positive value.
 * - status: Current operational status of the combo (e.g., ACTIVE or INACTIVE).
 * - type: The type of the combo (e.g., DAILY or MONTHLY), impacting its frequency or duration.
 * - totalQuota: The total number of this combo available for sale, which must be positive.
 * - validFrom: The start date from which the combo is valid, with a constraint for present or future dates.
 * - validTo: Optional end date for the combo's validity, relevant for type MONTHLY.
 */
@Schema(description = "Data Transfer Object for creating or updating a Combo offering")
public record ComboRequestDTO (
        @Schema(description = "Name of the combo", example = "Monthly Pass November 2025",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
        String name,

        @Schema(description = "Detailed description",
                example = "Provides lunch access for all weekdays in November",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @Schema(description = "Price of the combo", example = "150000.00",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @Schema(description = "Status of the combo (ACTIVE, INACTIVE)", example = "ACTIVE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Status is required")
        ComboStatus status,

        @Schema(description = "Type of combo (DAILY or MONTHLY)", example = "MONTHLY",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Type is required")
        ComboType type,

        @Schema(description = "Total number available for sale", example = "140",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Total quota is required")
        @Positive(message = "Total quota must be positive")
        Integer totalQuota,

        @Schema(description = "The first date this combo is valid", example = "2025-11-01",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "ValidFrom date is required")
        @FutureOrPresent(message = "ValidFrom date must be in the present or future")
        LocalDate validFrom,

        @Schema(description = "The last date this combo is valid (Required only for MONTHLY type)",
                example = "2025-11-30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDate validTo
){
}
