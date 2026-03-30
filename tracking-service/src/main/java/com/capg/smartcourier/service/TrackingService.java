package com.capg.smartcourier.service;

import com.capg.smartcourier.entity.Tracking;
import com.capg.smartcourier.repository.TrackingRepository;
import com.capg.smartcourier.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository repo;

    // ADD TRACKING
    public Tracking addTracking(Tracking tracking) {
        return repo.save(tracking);
    }

    // GET TRACKING HISTORY
    public List<Tracking> getTracking(String trackingNumber) {

        List<Tracking> list = repo.findByTrackingNumber(trackingNumber);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                "No tracking history found for tracking number: " + trackingNumber
            );
        }

        return list;
    }
}