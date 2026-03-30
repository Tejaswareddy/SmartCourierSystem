package com.capg.smartcourier.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeliveryProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deliveryId;
    private String proofType; // PHOTO, SIGNATURE, etc.
    private String proofData; // Base64 encoded or file path
    private LocalDateTime capturedAt;
    private String location;
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