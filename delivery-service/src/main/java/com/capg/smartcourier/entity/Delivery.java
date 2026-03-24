package com.capg.smartcourier.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingNumber;
    private Long userId;
    private String serviceType;
    private String status;
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