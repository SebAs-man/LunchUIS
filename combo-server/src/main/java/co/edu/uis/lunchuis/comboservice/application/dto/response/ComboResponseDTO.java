package co.edu.uis.lunchuis.comboservice.application.dto.response;

import co.edu.uis.lunchuis.common.enums.ComboStatus;
import co.edu.uis.lunchuis.common.enums.ComboType;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Represents a Data Transfer Object (DTO) for Combo entities, encapsulating essential information
 * about a combo to be shared across application layers. This DTO is utilized for communication
 * between the back-end and front-end or between service layers, without exposing the domain model.
 * Fields:
 * - name: The name of the combo.
 * - description: A brief description or details about the combo.
 * - price: The price of the combo.
 * - imageUrl: The URL pointing to an image of the combo.
 * - status: The status of the combo, represented by {@link ComboStatus}.
 * - type: The type of the combo, represented by {@link ComboType}.
 * - totalQuota: The total number of combos available.
 * - availableQuota: The number of remaining combos available for purchase.
 * - validFrom: The start date and time when the combo becomes valid.
 * - validTo: The end date and time when the combo is no longer valid.
 */
public record ComboResponseDTO(
        String name,
        String description,
        Double price,
        String imageUrl,
        ComboStatus status,
        ComboType type,
        Integer totalQuota,
        Integer availableQuota,
        LocalDate validFrom,
        LocalDate validTo
) {
}
