package com.capg.smartcourier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.smartcourier.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}