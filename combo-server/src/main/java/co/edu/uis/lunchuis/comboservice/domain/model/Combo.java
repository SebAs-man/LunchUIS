package co.edu.uis.lunchuis.comboservice.domain.model;


import co.edu.uis.lunchuis.common.enums.ComboStatus;

import java.time.Instant;
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
    private Double price;
    private LocalDate availableDate;
    private Integer totalStock;
    private Integer availableStock;
    private ComboStatus status;
    private String imageUrl;
    private final Instant createdAt;
    private Instant updatedAt;

    public Combo(UUID id, Integer totalStock, String name, String description,
                 Double price, LocalDate availableDate, String imageUrl,
                 ComboStatus status) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.createdAt = Instant.now();
        setTotalStock(totalStock);
        setName(name);
        setDescription(description);
        setPrice(price);
        setAvailableDate(availableDate);
        setImageUrl(imageUrl);
        setStatus(status);
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
    public Double getPrice() {
        return price;
    }
    public LocalDate getAvailableDate() {
        return availableDate;
    }
    public Integer getTotalStock() {
        return totalStock;
    }
    public Integer getAvailableStock() {
        return availableStock;
    }
    public ComboStatus getStatus() {
        return status;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // --- Setters ---

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }
    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }
    public void setPrice(Double price) {
        this.price = Objects.requireNonNull(price);
    }
    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = Objects.requireNonNull(availableDate);
    }
    public void setTotalStock(Integer totalStock) {
        this.totalStock = Objects.requireNonNull(totalStock);
    }
    public void setAvailableStock(Integer availableStock) {
        if(availableStock == null){
            availableStock = this.totalStock;
        }
        this.availableStock = availableStock;
    }
    public void setStatus(ComboStatus status) {
        if(status == null){
            status = ComboStatus.AVAILABLE;
        }
        this.status = status;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
