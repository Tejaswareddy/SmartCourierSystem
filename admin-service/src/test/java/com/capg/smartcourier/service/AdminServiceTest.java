package com.capg.smartcourier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capg.smartcourier.feign.DeliveryClient;
import com.capg.smartcourier.feign.TrackingClient;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private DeliveryClient deliveryClient;

    @Mock
    private TrackingClient trackingClient;

    @InjectMocks
    private AdminService service;



    // ✅ TEST 1: Get All Deliveries
    @Test
    void testGetAllDeliveries() {

        List<Object> mockList = Arrays.asList(new Object(), new Object());

        when(deliveryClient.getAllDeliveries()).thenReturn(mockList);

        List<Object> result = service.getAllDeliveries();

        assertEquals(2, result.size());
    }

    // ✅ TEST 2: Update Delivery
    @Test
    void testUpdateDelivery() {

        Map<String, String> body = new HashMap<>();
        body.put("status", "DELIVERED");

        Object mockResponse = new Object();

        when(deliveryClient.updateDelivery(1L, body)).thenReturn(mockResponse);

        Object result = service.updateDelivery(1L, body);

        assertNotNull(result);
    }

    // ✅ TEST 3: Get Tracking
    @Test
    void testGetTracking() {

        List<Object> mockTracking = Arrays.asList(new Object());

        when(trackingClient.getTracking("ABC123")).thenReturn(mockTracking);

        List<Object> result = service.getTracking("ABC123");

        assertEquals(1, result.size());
    }

    // ❌ TEST 4: Delivery Service Failure (Feign Exception)
    @Test
    void testGetAllDeliveriesFailure() {

        when(deliveryClient.getAllDeliveries())
                .thenThrow(new RuntimeException("Service Down"));

        assertThrows(RuntimeException.class, () -> {
            service.getAllDeliveries();
        });
    }

    // ❌ TEST 5: Tracking Service Failure
    @Test
    void testGetTrackingFailure() {

        when(trackingClient.getTracking("ABC123"))
                .thenThrow(new RuntimeException("Tracking Service Down"));

        assertThrows(RuntimeException.class, () -> {
            service.getTracking("ABC123");
        });
    }
}