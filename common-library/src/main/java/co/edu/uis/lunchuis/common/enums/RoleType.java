package co.edu.uis.lunchuis.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Defines roles that can be assigned to a user within the system.
 * The RoleType enum specifies a set of predefined constants representing
 * the different permission levels or responsibilities assigned to a user.
 * These roles help in managing access control and determining
 * permissible actions for users based on their designated role.
 */
@Schema(description = "Enumeration of possible role names.")
public enum RoleType {
    STUDENT,
    ADMIN
}
