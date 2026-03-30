package com.capg.smartcourier.entity;

import jakarta.persistence.*;

@Entity
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String address;
    private String contactNumber;
    private String manager;
    private boolean active;

    public Hub() {}

    public Hub(String name, String location, String address, String contactNumber, String manager) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.contactNumber = contactNumber;
        this.manager = manager;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}