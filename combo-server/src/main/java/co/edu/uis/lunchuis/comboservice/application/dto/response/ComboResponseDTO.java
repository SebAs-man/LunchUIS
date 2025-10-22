package co.edu.uis.lunchuis.comboservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;

}
