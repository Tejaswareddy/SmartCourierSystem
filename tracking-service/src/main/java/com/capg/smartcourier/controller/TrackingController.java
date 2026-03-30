package com.capg.smartcourier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import com.capg.smartcourier.entity.Tracking;
import com.capg.smartcourier.entity.Document;
import com.capg.smartcourier.entity.DeliveryProof;
import com.capg.smartcourier.service.TrackingService;
import com.capg.smartcourier.service.DocumentService;
import com.capg.smartcourier.service.DeliveryProofService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Tracking API layer
// - Provides tracking queries and proof upload endpoints
// - Backs the tracking and document features of the system
@RestController
@RequestMapping({"/tracking", "/api/tracking"})
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DeliveryProofService deliveryProofService;

    @PostMapping
    public Tracking addTracking(@RequestBody Tracking tracking) {
        return trackingService.addTracking(tracking);
    }

    @GetMapping("/{trackingNumber}")
    public List<Tracking> getTracking(@PathVariable String trackingNumber) {
        return trackingService.getTracking(trackingNumber);
    }

    // Upload document
    @PostMapping("/documents/upload")
    public Document uploadDocument(@RequestParam("file") MultipartFile file,
                                   @RequestParam("deliveryId") Long deliveryId,
                                   @RequestParam("userId") Long userId) throws IOException {
        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        Document document = new Document(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            filePath.toString(),
            deliveryId,
            userId
        );

        return documentService.saveDocument(document);
    }

    // Get delivery proof
    @GetMapping("/{id}/proof")
    public List<DeliveryProof> getDeliveryProof(@PathVariable Long id) {
        return deliveryProofService.getDeliveryProofsByDeliveryId(id);
    }

    // Add delivery proof (deliveryId from path)
    @PostMapping("/{id}/proof")
    public DeliveryProof addDeliveryProof(@PathVariable Long id, @RequestBody DeliveryProof proof) {
        if (proof == null || proof.getProofData() == null || proof.getProofData().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proofData is required");
        }
        proof.setDeliveryId(id);
        return deliveryProofService.saveDeliveryProof(proof);
    }

    // Add delivery proof (deliveryId from body)
    @PostMapping("/proof")
    public DeliveryProof addDeliveryProof(@RequestBody DeliveryProof proof) {
        if (proof == null || proof.getDeliveryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deliveryId is required in payload");
        }
        if (proof.getProofData() == null || proof.getProofData().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proofData is required");
        }
        return deliveryProofService.saveDeliveryProof(proof);
    }
}