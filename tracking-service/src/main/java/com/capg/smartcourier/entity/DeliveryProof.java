package com.capg.smartcourier.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class DeliveryProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Delivery ID is required")
    @Positive(message = "Delivery ID must be a positive number")
    private Long deliveryId;

    @NotBlank(message = "Proof type is required")
    @Pattern(regexp = "^(PHOTO|SIGNATURE|VIDEO|DOCUMENT|OTHER)$", message = "Proof type must be PHOTO, SIGNATURE, VIDEO, DOCUMENT, or OTHER")
    private String proofType; // PHOTO, SIGNATURE, etc.

    @NotBlank(message = "Proof data is required")
    @Size(min = 10, message = "Proof data must not be empty")
    private String proofData; // Base64 encoded or file path

    private LocalDateTime capturedAt;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 200, message = "Location must be between 3 and 200 characters")
    private String location;

    @NotNull(message = "Captured by (User ID) is required")
    @Positive(message = "User ID must be a positive number")
    private Long capturedBy; // User ID who captured the proof

    public DeliveryProof() {}

    public DeliveryProof(Long deliveryId, String proofType, String proofData, String location, Long capturedBy) {
        this.deliveryId = deliveryId;
        this.proofType = proofType;
        this.proofData = proofData;
        this.location = location;
        this.capturedBy = capturedBy;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public String getProofType() { return proofType; }
    public void setProofType(String proofType) { this.proofType = proofType; }

    public String getProofData() { return proofData; }
    public void setProofData(String proofData) { this.proofData = proofData; }

    public LocalDateTime getCapturedAt() { return capturedAt; }
    public void setCapturedAt(LocalDateTime capturedAt) { this.capturedAt = capturedAt; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Long getCapturedBy() { return capturedBy; }
    public void setCapturedBy(Long capturedBy) { this.capturedBy = capturedBy; }
}