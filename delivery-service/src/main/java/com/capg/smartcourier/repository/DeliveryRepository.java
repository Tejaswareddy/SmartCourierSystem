package com.capg.smartcourier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.smartcourier.entity.Delivery;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUserId(Long userId);
}