package com.capg.smartcourier.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.smartcourier.service.DeliveryService;
import com.capg.smartcourier.entity.Delivery;

// Delivery API layer
// - Exposes REST endpoints for clients (frontend / gateway / other services)
// - Delegates business rules to DeliveryService
// - Supports create/read/update flows for delivery entities
@RestController
@RequestMapping({"/deliveries", "/api/deliveries"})
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    // CREATE
    @PostMapping
    public Delivery create(@RequestBody Delivery delivery) {
        return service.createDelivery(delivery);
    }

    // GET ALL
    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return service.getAllDeliveries();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Delivery getById(@PathVariable Long id) {
        return service.getDeliveryById(id);
    }

    // GET MY DELIVERIES
    @GetMapping("/my")
    public List<Delivery> getMyDeliveries(@RequestHeader("X-User-Id") Long userId) {
        return service.getDeliveriesByUserId(userId);
    }

    // UPDATE (for admin-service)
    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateDelivery(id, body.get("status"));
    }
}