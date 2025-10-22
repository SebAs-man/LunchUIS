package co.edu.uis.lunchuis.identityservice.domain.model;

import co.edu.uis.lunchuis.common.enums.RoleType;

import java.util.UUID;

/**
 * Represents a role within the system's domain model.
 * A {@code Role} defines the level of permissions or access a user has
 * within the application (for example: {@code ADMIN}, {@code STUDENT}).
 * It is a core domain entity that can be associated with one or more users.
 * This class is part of the domain layer, meaning it should not depend on
 * any external frameworks (such as JPA or Spring). Its purpose is purely
 * to model business concepts and enforce invariants related to roles.
 */
public record Role(UUID id, RoleType name) {
    /**
     * Constructs a new {@code Role} with the given parameters.
     *
     * @param id   the unique identifier of the role (must not be {@code null})
     * @param name the specific role name (must not be {@code null})
     */
    public Role(UUID id, RoleType name) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.name = (name != null) ? name : RoleType.STUDENT;
    }
}
