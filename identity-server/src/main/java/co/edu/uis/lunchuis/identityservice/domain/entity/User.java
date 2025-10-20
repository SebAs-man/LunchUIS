package co.edu.uis.lunchuis.identityservice.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a user of the LunchUIS application.
 * This entity implements Spring Security's UserDetails interface
 * for seamless integration with the authentication and authorization framework.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a user in the system.")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private Role role;

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

    @PrePersist
    public void onCreate() {
        this.enabled = true;
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
