package com.capg.smartcourier.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tracking number is required")
    @Pattern(regexp = "^[A-Z0-9]{10,20}$", message = "Tracking number must have 10-20 alphanumeric characters")
    private String trackingNumber;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotBlank(message = "Service type is required")
    @Pattern(regexp = "^(STANDARD|EXPRESS|OVERNIGHT|SAME_DAY)$", message = "Service type must be STANDARD, EXPRESS, OVERNIGHT, or SAME_DAY")
    private String serviceType;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(CREATED|PICKED|IN_TRANSIT|DELIVERED|FAILED|CANCELLED)$", message = "Status must be CREATED, PICKED, IN_TRANSIT, DELIVERED, FAILED, or CANCELLED")
    private String status;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Total amount must have at most 10 integer digits and 2 decimal places")
    private Double totalAmount;

    // 🔗 One Delivery → One Parcel
    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Parcel parcel;

    // 🔗 One Delivery → Many Addresses
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<Address> addresses;

    public Delivery() {}

    public Delivery(Long id, String trackingNumber, Long userId,
                    String serviceType, String status, Double totalAmount) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.userId = userId;
        this.serviceType = serviceType;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Parcel getParcel() { return parcel; }
    public void setParcel(Parcel parcel) { this.parcel = parcel; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
}