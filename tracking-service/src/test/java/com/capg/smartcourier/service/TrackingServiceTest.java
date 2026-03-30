package com.capg.smartcourier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.capg.smartcourier.entity.Tracking;
import com.capg.smartcourier.exception.ResourceNotFoundException;
import com.capg.smartcourier.repository.TrackingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackingServiceTest {

    @Mock
    private TrackingRepository repo;

    @InjectMocks
    private TrackingService service;



    // ✅ TEST 1: Add Tracking
    @Test
    void testAddTracking() {

        Tracking tracking = new Tracking();
        tracking.setTrackingNumber("TRK123");

        when(repo.save(tracking)).thenReturn(tracking);

        Tracking result = service.addTracking(tracking);

        assertEquals("TRK123", result.getTrackingNumber());
        verify(repo, times(1)).save(tracking);
    }

    // ✅ TEST 2: Get Tracking History (Success)
    @Test
    void testGetTrackingSuccess() {

        Tracking t1 = new Tracking();
        t1.setTrackingNumber("TRK123");

        Tracking t2 = new Tracking();
        t2.setTrackingNumber("TRK123");

        List<Tracking> list = Arrays.asList(t1, t2);

        when(repo.findByTrackingNumber("TRK123")).thenReturn(list);

        List<Tracking> result = service.getTracking("TRK123");

        assertEquals(2, result.size());
        verify(repo, times(1)).findByTrackingNumber("TRK123");
    }

    // ❌ TEST 3: Get Tracking History (Not Found)
    @Test
    void testGetTrackingNotFound() {

        when(repo.findByTrackingNumber("TRK123"))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getTracking("TRK123");
        });

        verify(repo, times(1)).findByTrackingNumber("TRK123");
    }
}