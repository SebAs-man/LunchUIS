package co.edu.uis.lunchuis.identityservice.domain.entity;

import co.edu.uis.lunchuis.identityservice.domain.valueobject.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a user role in the system (e.g., STUDENT, ADMIN).")
public final class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier of the role.",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(length = 20, nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Name of the role.", example = "STUDENT",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private RoleType name;
}
