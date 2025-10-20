package co.edu.uis.lunchuis.identityservice.domain.model;

import co.edu.uis.lunchuis.common.enums.RoleType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the business domain.
 * This class models the core business rules for user management,
 * independent of persistence or framework concerns.
 */
public final class User {
    private final UUID id;
    private String firstName;
    private String lastName;
    private Integer institutionalCode;
    private String email;
    private String password;
    private Role role;
    private Boolean enabled;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructs a new {@code User} instance with the specified parameters.
     * Automatically initializes default values for non-provided fields.
     * @param id                the unique identifier of the user; if {@code null},
     *                          a new random ID is generated
     * @param role              the role assigned to the user; if {@code null},
     *                          defaults to a {@code Role} of {@code STUDENT} type
     * @param firstName         the first name of the user (must not be {@code null})
     * @param lastName          the last name of the user (must not be {@code null})
     * @param institutionalCode a unique institutional code assigned to the user,
     *                          often used for identification purposes (must not be {@code null})
     * @param email             the email address of the user (must not be {@code null})
     * @param password          the password for the user account (must not be {@code null})
     * @param enabled           the activation status of the user; defaults to {@code true} if {@code null}
     */
    public User(UUID id, Role role, String firstName,
                String lastName, Integer institutionalCode, String email, String password,
                Boolean enabled) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.createdAt = Instant.now();
        setFirstName(firstName);
        setLastName(lastName);
        setInstitutionalCode(institutionalCode);
        setEmail(email);
        setPassword(password);
        setRole(role);
        setEnabled(enabled);
    }

    // --- Getters ---
    public UUID getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Integer getInstitutionalCode() {
        return institutionalCode;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Role getRole() {
        return role;
    }
    public Boolean isEnabled() {
        return enabled;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // --- Setters ---

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName);
    }
    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName);
    }
    public void setInstitutionalCode(Integer institutionalCode) {
        this.institutionalCode = Objects.requireNonNull(institutionalCode);
    }
    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email);
    }
    public void setEnabled(Boolean enabled) {
        if(enabled == null){
            enabled = true;
        }
        this.enabled = enabled;
    }
    public void setPassword(String password) {
        this.password = Objects.requireNonNull(password);
    }
    public void setRole(Role role) {
        if(role == null){
            role = new Role(null, RoleType.STUDENT);
        }
        this.role = role;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
