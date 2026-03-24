package com.capg.smartcourier.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.smartcourier.entity.Delivery;
import com.capg.smartcourier.repository.DeliveryRepository;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository repo;
    
    public Delivery updateDelivery(Long id, Map<String, String> body) {

        Delivery delivery = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        // Update fields (only if present)
        if (body.containsKey("status")) {
            delivery.setStatus(body.get("status"));
        }

        if (body.containsKey("serviceType")) {
            delivery.setServiceType(body.get("serviceType"));
        }

        if (body.containsKey("totalAmount")) {
            delivery.setTotalAmount(Double.parseDouble(body.get("totalAmount")));
        }

        return repo.save(delivery);
    }

    public Delivery createDelivery(Delivery delivery) {

        // 🔥 IMPORTANT: Set relationships manually

        if (delivery.getParcel() != null) {
            delivery.getParcel().setDelivery(delivery);
        }

        if (delivery.getAddresses() != null) {
            delivery.getAddresses().forEach(addr -> addr.setDelivery(delivery));
        }

        return repo.save(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return repo.findAll();
    }
}