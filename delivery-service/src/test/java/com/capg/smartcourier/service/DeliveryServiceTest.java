package com.capg.smartcourier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capg.smartcourier.entity.Delivery;
import com.capg.smartcourier.exception.ResourceNotFoundException;
import com.capg.smartcourier.messaging.MessageProducer;
import com.capg.smartcourier.repository.DeliveryRepository;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository repo;
    
    @Mock
    private MessageProducer producer;

    @InjectMocks
    private DeliveryService service;



    // ✅ TEST 1: Create Delivery
    @Test
    void testCreateDelivery() {

        Delivery delivery = new Delivery();
        delivery.setStatus("CREATED");

        when(repo.save(delivery)).thenReturn(delivery);

        Delivery result = service.createDelivery(delivery);

        assertEquals("CREATED", result.getStatus());
        verify(repo, times(1)).save(delivery);
    }

    // ✅ TEST 2: Get All Deliveries (Success)
    @Test
    void testGetAllDeliveriesSuccess() {

        List<Delivery> list = Arrays.asList(new Delivery(), new Delivery());

        when(repo.findAll()).thenReturn(list);

        List<Delivery> result = service.getAllDeliveries();

        assertEquals(2, result.size());
    }

    // ❌ TEST 3: Get All Deliveries (Empty → Exception)
    @Test
    void testGetAllDeliveriesEmpty() {

        when(repo.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getAllDeliveries();
        });
    }

    // ✅ TEST 4: Get Delivery By ID (Success)
    @Test
    void testGetDeliveryByIdSuccess() {

        Delivery delivery = new Delivery();
        delivery.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(delivery));

        Delivery result = service.getDeliveryById(1L);

        assertEquals(1L, result.getId());
    }

    // ❌ TEST 5: Get Delivery By ID (Not Found)
    @Test
    void testGetDeliveryByIdNotFound() {

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getDeliveryById(1L);
        });
    }

    // ✅ TEST 6: Update Delivery Status (Success)
    @Test
    void testUpdateDeliverySuccess() {

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setStatus("CREATED");

        when(repo.findById(1L)).thenReturn(Optional.of(delivery));
        when(repo.save(delivery)).thenReturn(delivery);

        Delivery result = service.updateDelivery(1L, "DELIVERED");

        assertEquals("DELIVERED", result.getStatus());
        verify(repo).save(delivery);
    }

    // ❌ TEST 7: Update Delivery (Not Found)
    @Test
    void testUpdateDeliveryNotFound() {

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateDelivery(1L, "DELIVERED");
        });
    }
}