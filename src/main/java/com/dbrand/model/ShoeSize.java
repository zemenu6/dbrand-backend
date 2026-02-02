package com.dbrand.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShoeSize {
    private UUID id;
    private UUID shoeId;
    private Integer size;
    private Integer stockCount;
    private LocalDateTime createdAt;

    // Constructors
    public ShoeSize() {}

    public ShoeSize(UUID shoeId, Integer size, Integer stockCount) {
        this.shoeId = shoeId;
        this.size = size;
        this.stockCount = stockCount;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getShoeId() { return shoeId; }
    public void setShoeId(UUID shoeId) { this.shoeId = shoeId; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Integer getStockCount() { return stockCount; }
    public void setStockCount(Integer stockCount) { this.stockCount = stockCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
