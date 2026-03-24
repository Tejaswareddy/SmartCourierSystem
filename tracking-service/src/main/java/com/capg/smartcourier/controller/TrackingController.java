package com.capg.smartcourier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.smartcourier.entity.Tracking;
import com.capg.smartcourier.service.TrackingService;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService service;

    @PostMapping
    public Tracking addTracking(@RequestBody Tracking tracking) {
        return service.addTracking(tracking);
    }

    @GetMapping("/{trackingNumber}")
    public List<Tracking> getTracking(@PathVariable String trackingNumber) {
        return service.getTracking(trackingNumber);
    }
}