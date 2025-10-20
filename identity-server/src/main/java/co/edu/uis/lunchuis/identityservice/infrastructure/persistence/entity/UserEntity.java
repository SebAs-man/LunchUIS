package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents the database entity for users within the system.
 * This class maps directly to the "users" table in PostgresSQL.
 * It contains persistent attributes, mapping annotations, and lifecycle callbacks.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a user record stored in the database.")
public final class UserEntity {
    @Id
    @Schema(description = "Unique identifier of the user.",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "User's first name.", example = "Carlos",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "User's last name.", example = "Beltrán",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Column(name = "institutional_code", nullable = false, unique = true)
    @Schema(description = "User's unique institutional code.", example = "2180001",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer institutionalCode;

    @Column(nullable = false, unique = true)
    @Schema(description = "User's unique institutional email.", example = "carlos.beltran@uis.edu.co",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Column(nullable = false)
    @Schema(description = "User's hashed password.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER fetch is fine for a single role
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @Schema(description = "The role assigned to the user.", requiredMode = Schema.RequiredMode.REQUIRED)
    private RoleEntity role;

    @Column(nullable = false)
    @Schema(description = "Indicates whether the user is enabled or disabled.",
            example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the user was created.")
    private Instant createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the user was last updated.")
    private Instant updatedAt;
}
