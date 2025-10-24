package co.edu.uis.lunchuis.comboservice.application.mapper;

import co.edu.uis.lunchuis.comboservice.application.dto.request.ComboRequestDTO;
import co.edu.uis.lunchuis.comboservice.domain.model.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    Combo toDomain(ComboRequestDTO request);
}
