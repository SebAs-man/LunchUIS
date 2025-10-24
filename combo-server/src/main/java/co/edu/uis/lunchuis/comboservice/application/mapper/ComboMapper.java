package co.edu.uis.lunchuis.comboservice.application.mapper;

import co.edu.uis.lunchuis.comboservice.application.dto.request.ComboRequestDTO;
import co.edu.uis.lunchuis.comboservice.application.dto.response.ComboResponseDTO;
import co.edu.uis.lunchuis.comboservice.domain.model.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * ComboMapper is a MapStruct interface responsible for mapping data between
 * the ComboRequestDTO object and the Combo domain model. It simplifies the
 * transformation of data transfer objects into domain entities, ensuring
 * seamless integration between layers in the application.
 */
@Mapper(componentModel = "spring")
public interface ComboMapper {
    /**
     * Converts a given ComboRequestDTO object into a Combo domain model object.
     * The mapping process copies all relevant properties from the DTO to the
     * domain object, excluding the "validTo" field, which is explicitly ignored.
     * @param request the ComboRequestDTO object containing data to be mapped
     *                to a Combo domain object
     * @return a Combo domain object created from the provided ComboRequestDTO
     */
    @Mapping(target = "validTo", ignore = true)
    @Mapping(target = "availableQuota", ignore = true)
    Combo toDomain(ComboRequestDTO request);

    /**
     * Maps a {@link Combo} domain model to a {@link ComboResponseDTO}.
     * @param combo The {@link Combo} domain model.
     * @return The mapped {@link ComboResponseDTO}.
     */
    ComboResponseDTO toResponse(Combo combo);

    /**
     * Updates an existing {@link Combo} domain model from a {@link ComboRequestDTO}.
     * @param dto   The DTO containing the updated data.
     * @param model The domain model entity to be updated.
     */
    @Mapping(target = "availableQuota", ignore = true)
    @Mapping(target = "validTo", ignore = true)
    void updateModelFromDto(ComboRequestDTO dto, @MappingTarget Combo model);
}
