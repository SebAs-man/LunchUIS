package co.edu.uis.lunchuis.identityservice.application.dto.request;

import co.edu.uis.lunchuis.common.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating user role.
 * This DTO is used when administrators need to change a user's role.
 */
@Schema(description = "DTO for updating user role")
public record UpdateUserRoleRequest(
        @Schema(description = "New role type", example = "ADMIN")
        @NotNull(message = "Role type cannot be null")
        RoleType roleType
) {}
