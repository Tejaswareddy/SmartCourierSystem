package com.capg.smartcourier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.smartcourier.entity.Tracking;
import com.capg.smartcourier.repository.TrackingRepository;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository repo;

    public Tracking addTracking(Tracking tracking) {
        return repo.save(tracking);
    }

    public List<Tracking> getTracking(String trackingNumber) {
        return repo.findByTrackingNumberOrderByIdAsc(trackingNumber);
    }
}