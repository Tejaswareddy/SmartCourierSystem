package com.capg.smartcourier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.smartcourier.entity.Delivery;
import com.capg.smartcourier.event.TrackingEvent;
import com.capg.smartcourier.exception.ResourceNotFoundException;
import com.capg.smartcourier.messaging.MessageProducer;
import com.capg.smartcourier.repository.DeliveryRepository;

// Core delivery orchestration logic
// - CRUD operations for Delivery
// - Event publishing on create (for tracking / other async workflows)
// - Performs domain consistency (cascade relations between Delivery, Parcel, Address)
@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repo;

    @Autowired
    private MessageProducer producer;

    // ✅ CREATE DELIVERY + SEND EVENT
    public Delivery createDelivery(Delivery delivery) {

        // 🔗 Set relationships properly
        if (delivery.getParcel() != null) {
            delivery.getParcel().setDelivery(delivery);
        }

        if (delivery.getAddresses() != null) {
            delivery.getAddresses().forEach(addr -> addr.setDelivery(delivery));
        }
        
        // Set default status at creation time; for now create is always 'CREATED'.
// Note: existing code path checks delivery.getStatus() != null, but we always override.
// Keep in mind if fields come in as null additional model validation may be needed.
if (delivery.getStatus() != null) {
            delivery.setStatus("CREATED");
        }

        // 💾 Save delivery
        Delivery saved = repo.save(delivery);

        // 📤 Send message to RabbitMQ
        TrackingEvent event = new TrackingEvent(
                saved.getTrackingNumber(),
                "CREATED",
                "Warehouse",
                "Order created"
        );

        producer.sendTrackingEvent(event);

        return saved;
    }

    // ✅ GET ALL
    public List<Delivery> getAllDeliveries() {

        List<Delivery> list = repo.findAll();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No deliveries found");
        }

        return list;
    }

    // ✅ GET BY ID
    public Delivery getDeliveryById(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    // ✅ UPDATE DELIVERY (Admin use)
    public Delivery updateDelivery(Long id, String status) {

        Delivery delivery = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery not found with id: " + id));

        delivery.setStatus(status);

        return repo.save(delivery);
    }

    // ✅ GET DELIVERIES BY USER ID
    public List<Delivery> getDeliveriesByUserId(Long userId) {

        List<Delivery> list = repo.findByUserId(userId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No deliveries found for user: " + userId);
        }

        return list;
    }
}