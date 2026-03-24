package com.capg.smartcourier.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.smartcourier.entity.Delivery;
import com.capg.smartcourier.service.DeliveryService;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService service;
    
    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateDelivery(id, body);
    }

    @PostMapping
    public Delivery create(@RequestBody Delivery delivery) {
        return service.createDelivery(delivery);
    }

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return service.getAllDeliveries();
    }
}