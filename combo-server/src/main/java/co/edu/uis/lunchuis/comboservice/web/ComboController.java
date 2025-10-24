package co.edu.uis.lunchuis.comboservice.web;

import co.edu.uis.lunchuis.comboservice.application.dto.request.ComboRequestDTO;
import co.edu.uis.lunchuis.comboservice.application.dto.response.ComboResponseDTO;
import co.edu.uis.lunchuis.comboservice.application.service.ComboService;
import co.edu.uis.lunchuis.common.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("combos")
public class ComboController {
    private final ComboService comboService;

    @Operation(summary = "Create a new Combo", description = "Creates a new combo offering. Requires ADMIN role.")
    // ... (ApiResponses)
    @PostMapping
    public ResponseEntity<ComboResponseDTO> createCombo(@Valid @RequestBody ComboRequestDTO requestDTO) {
        ComboResponseDTO createdCombo = comboService.createCombo(requestDTO);
        return new ResponseEntity<>(createdCombo, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Combo by ID", description = "Retrieves a specific combo by its unique ID. Requires ADMIN role.")
    // ... (ApiResponses)
    @GetMapping("/{id}")
    public ResponseEntity<ComboResponseDTO> getComboById(@PathVariable UUID id) {
        return ResponseEntity.ok(comboService.getComboById(id));
    }

    @Operation(summary = "Get all Combos", description = "Retrieves a list of all combo offerings. Requires ADMIN role.")
    // ... (ApiResponses)
    @GetMapping
    public ResponseEntity<List<ComboResponseDTO>> getAllCombos() {
        return ResponseEntity.ok(comboService.getAllCombos());
    }

    @Operation(summary = "Update an existing Combo", description = "Updates a combo by its ID. Requires ADMIN role.")
    // ... (ApiResponses)
    @PutMapping("/{id}")
    public ResponseEntity<ComboResponseDTO> updateCombo(@PathVariable UUID id, @Valid @RequestBody ComboRequestDTO requestDTO) {
        return ResponseEntity.ok(comboService.updateCombo(id, requestDTO));
    }

    @Operation(summary = "Delete a Combo", description = "Deletes a combo by its ID. Requires ADMIN role.")
    // ... (ApiResponses)
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCombo(@PathVariable UUID id) {
        comboService.deleteCombo(id);
        return ResponseEntity.ok(new MessageResponse("Combo with ID " + id + " deleted successfully."));
    }

}
