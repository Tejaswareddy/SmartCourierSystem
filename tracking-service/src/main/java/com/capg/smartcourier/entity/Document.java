package com.capg.smartcourier.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    private String fileName;

    @NotBlank(message = "File type is required")
    @Pattern(regexp = "^[a-zA-Z0-9/\\-+]{3,50}$", message = "File type must be valid (e.g., application/pdf)")
    private String fileType;

    @NotNull(message = "File size is required")
    @Positive(message = "File size must be greater than 0")
    @Max(value = 104857600, message = "File size must not exceed 100 MB")
    private Long fileSize;

    @NotBlank(message = "File path is required")
    @Size(min = 3, max = 500, message = "File path must be between 3 and 500 characters")
    private String filePath;

    @NotNull(message = "Delivery ID is required")
    @Positive(message = "Delivery ID must be a positive number")
    private Long deliveryId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    private LocalDateTime uploadedAt;

    public Document() {}

    public Document(String fileName, String fileType, Long fileSize, String filePath, Long deliveryId, Long userId) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.deliveryId = deliveryId;
        this.userId = userId;
        this.uploadedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}