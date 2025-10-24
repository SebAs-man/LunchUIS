package co.edu.uis.lunchuis.comboservice.domain.model;


import co.edu.uis.lunchuis.common.enums.ComboStatus;
import co.edu.uis.lunchuis.common.enums.ComboType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a combo item which includes details such as name, description,
 * price, image URL, and timestamps for creation and updates. A UUID
 * identifies each combo uniquely.
 */
public final class Combo {
    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private ComboStatus status;
    private final ComboType type;
    private Integer totalQuota;
    private Integer availableQuota;
    private LocalDate validFrom;
    private LocalDate validTo;

    /**
     * Constructs an instance of the Combo class.
     * @param id            The unique identifier for the combo. If null, a new UUID is generated.
     * @param type          The type of the combo, represented by the ComboType enum.
     * @param name          The name of the combo.
     * @param description   A brief description of the combo.
     * @param price         The price of the combo.
     * @param status        The status of the combo, represented by the ComboStatus enum.
     * @param totalQuota    The total quota of the combo available.
     * @param availableQuota The number of quotas still available.
     * @param validFrom     The start date from which the combo is valid.
     * @param validTo       The end date until which the combo is valid.
     */
    public Combo(UUID id, ComboType type, String name, String description, BigDecimal price,
                 ComboStatus status, Integer totalQuota, Integer availableQuota, LocalDate validFrom,
                 LocalDate validTo) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.type = Objects.requireNonNull(type, "Status cannot be null");
        setName(name);
        setDescription(description);
        setPrice(price);
        setTotalQuota(totalQuota);
        setAvailableQuota(availableQuota);
        setValidFrom(validFrom);
        setValidTo(validTo);
    }

    // --- Getters ---

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public ComboStatus getStatus() {
        return status;
    }
    public ComboType getType() {
        return type;
    }
    public Integer getTotalQuota() {
        return totalQuota;
    }
    public Integer getAvailableQuota() {
        return availableQuota;
    }
    public LocalDate getValidFrom() {
        return validFrom;
    }
    public LocalDate getValidTo() {
        return validTo;
    }

    // --- Setters ---

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(BigDecimal price) {
        this.price = Objects.requireNonNull(price, "Price cannot be null");
    }
    public void setStatus(ComboStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }
    public void setTotalQuota(Integer totalQuota) {
        this.totalQuota = Objects.requireNonNull(totalQuota, "Total quota cannot be null");
    }
    public void setAvailableQuota(Integer availableQuota) {
        if(availableQuota == null){
            availableQuota = this.totalQuota;
        }
        this.availableQuota = availableQuota;
    }
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = Objects.requireNonNull(validFrom, "Valid from cannot be null");
    }
    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
