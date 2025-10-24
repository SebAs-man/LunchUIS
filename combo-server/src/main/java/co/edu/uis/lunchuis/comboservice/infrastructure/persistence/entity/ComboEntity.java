package co.edu.uis.lunchuis.comboservice.infrastructure.persistence.entity;

import co.edu.uis.lunchuis.common.enums.ComboStatus;
import co.edu.uis.lunchuis.common.enums.ComboType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "combos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a combo record stored in the database.")
public class ComboEntity {
    @Id
    @Schema(description = "Unique identifier of the combo.",
            example = "f47ac10b-58cc-4372-a567-0e02")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private ComboStatus status;

    @Column(nullable = false, updatable = false)
    private ComboType type;

    @Column(name="total_quota", nullable = false)
    private Integer totalQuota;

    @Column(name="available_quota", nullable = false)
    private Integer availableQuota;

    @Column(name="valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name="valid_to", nullable = false)
    private LocalDate validTo;
}
