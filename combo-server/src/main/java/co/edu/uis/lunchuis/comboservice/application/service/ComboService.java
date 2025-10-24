package co.edu.uis.lunchuis.comboservice.application.service;

import co.edu.uis.lunchuis.comboservice.application.dto.request.ComboRequestDTO;
import co.edu.uis.lunchuis.comboservice.application.dto.response.ComboResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Application Service Port for managing Combos.
 * Defines the use cases for the Combo domain.
 */
public interface ComboService {
    /**
     * Creates a new Combo based on the provided DTO.
     * @param requestDTO DTO containing data for the new Combo.
     * @return DTO of the newly created Combo.
     */
    ComboResponseDTO createCombo(ComboRequestDTO requestDTO);

    /**
     * Updates an existing Combo identified by its ID.
     * @param id         The ID of the Combo to update.
     * @param requestDTO DTO containing the updated data.
     * @return DTO of the updated Combo.
     */
    ComboResponseDTO updateCombo(UUID id, ComboRequestDTO requestDTO);

    /**
     * Deletes a Combo by its ID.
     * @param id The ID of the Combo to delete.
     */
    void deleteCombo(UUID id);

    /**
     * Retrieves a Combo by its ID.
     * @param id The ID of the Combo to retrieve.
     * @return DTO of the found Combo.
     */
    ComboResponseDTO getComboById(UUID id);

    /**
     * Retrieves a list of all existing Combos.
     * @return A list of DTOs for all Combos.
     */
    List<ComboResponseDTO> getAllCombos();
}
