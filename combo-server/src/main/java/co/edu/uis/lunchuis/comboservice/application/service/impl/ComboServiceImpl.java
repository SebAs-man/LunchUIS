package co.edu.uis.lunchuis.comboservice.application.service.impl;

import co.edu.uis.lunchuis.comboservice.application.dto.request.ComboRequestDTO;
import co.edu.uis.lunchuis.comboservice.application.dto.response.ComboResponseDTO;
import co.edu.uis.lunchuis.comboservice.application.mapper.ComboMapper;
import co.edu.uis.lunchuis.comboservice.application.service.ComboService;
import co.edu.uis.lunchuis.comboservice.domain.model.Combo;
import co.edu.uis.lunchuis.comboservice.domain.repository.ComboRepository;
import co.edu.uis.lunchuis.common.enums.ComboType;
import co.edu.uis.lunchuis.common.exception.DuplicateResourceException;
import co.edu.uis.lunchuis.common.exception.InvalidRequestException;
import co.edu.uis.lunchuis.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ComboService} interface.
 * Contains all business logic for managing Combos.
 */
@Service
@RequiredArgsConstructor
public class ComboServiceImpl implements ComboService {
    private final ComboRepository comboRepository;
    private final ComboMapper comboMapper;

    @Override
    @Transactional
    public ComboResponseDTO createCombo(ComboRequestDTO requestDTO) {
        // 1. Map DTO to Domain Model
        Combo combo = comboMapper.toDomain(requestDTO);
        // 2. Apply Business Logic & Validations
        validateAndNormalize(combo, null);
        // 3. Set initial available quota
        combo.setAvailableQuota(combo.getTotalQuota());
        // 4. Save to persistence
        Combo savedCombo = comboRepository.save(combo);
        // 5. Map to Response DTO and return
        return comboMapper.toResponse(savedCombo);
    }

    @Override
    @Transactional
    public ComboResponseDTO updateCombo(UUID id, ComboRequestDTO requestDTO) {
        // 1. Find an existing combo
        Combo existingCombo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo", "id", id.toString()));
        // 2. Calculate sold items before update
        int soldCount = existingCombo.getTotalQuota() - existingCombo.getAvailableQuota();
        // 3. Map updated data onto the existing model
        comboMapper.updateModelFromDto(requestDTO, existingCombo);
        // 4. Apply Business Logic & Validations
        validateAndNormalize(existingCombo, id); // Use overloaded method for updates
        // 5. Apply complex quota logic
        if (requestDTO.totalQuota() < soldCount) {
            throw new InvalidRequestException("TotalQuota cannot be less than the number of combos already sold (" + soldCount + ")");
        }
        existingCombo.setAvailableQuota(requestDTO.totalQuota() - soldCount);
        // 6. Save updated combo
        Combo updatedCombo = comboRepository.save(existingCombo);
        // 7. Map to Response DTO and return
        return comboMapper.toResponse(updatedCombo);
    }

    @Override
    @Transactional
    public void deleteCombo(UUID id) {
        // 1. Find an existing combo
        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo", "id", id.toString()));
        // 2. Business Rule: Cannot delete it if orders have been placed
        if (combo.getAvailableQuota() < combo.getTotalQuota()) {
            throw new InvalidRequestException("Cannot delete Combo with ID " + id + ": orders have already been placed.");
        }
        // 3. Delete from persistence
        comboRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ComboResponseDTO getComboById(UUID id) {
        return comboRepository.findById(id)
                .map(comboMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Combo", "id", id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComboResponseDTO> getAllCombos() {
        return comboRepository.findAll().stream()
                .map(comboMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Shared validation logic for creating and updating combos.
     *
     * @param combo        The combo model to validate.
     * @param currentId    The ID of the combo being updated (null if creating).
     */
    private void validateAndNormalize(Combo combo, UUID currentId) {
        // 1. Normalize Name
        combo.setName(combo.getName().toUpperCase());
        // 2. Check for unique name
        Optional<Combo> existingByName = comboRepository.findByName(combo.getName());
        if (existingByName.isPresent() && (currentId == null || !existingByName.get().getId().equals(currentId))) {
            throw new DuplicateResourceException("Combo", "name", combo.getName());
        }
        // 3. Validate Dates based on Type
        if (combo.getType() == ComboType.DAILY) {
            // For DAILY, validTo must be the same as validFrom
            combo.setValidTo(combo.getValidFrom());
        } else if (combo.getType() == ComboType.MONTHLY) {
            // For MONTHLY, validTo must be provided and be after validFrom
            if (combo.getValidTo() == null) {
                throw new InvalidRequestException("validTo is required for MONTHLY combos.");
            }
            if (combo.getValidTo().isBefore(combo.getValidFrom())) {
                throw new InvalidRequestException("validTo date cannot be before validFrom date.");
            }
        }
    }
}
