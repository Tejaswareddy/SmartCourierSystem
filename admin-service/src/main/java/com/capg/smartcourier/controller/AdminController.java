package com.capg.smartcourier.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.smartcourier.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;
    
    @PutMapping("/delivery/{id}")
    public Object updateDelivery(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateDelivery(id, body);
    }

    @GetMapping("/deliveries")
    public List<Object> getAllDeliveries() {
        return service.getAllDeliveries();
    }

    @GetMapping("/tracking/{trackingNumber}")
    public List<Object> getTracking(@PathVariable String trackingNumber) {
        return service.getTracking(trackingNumber);
    }
}
