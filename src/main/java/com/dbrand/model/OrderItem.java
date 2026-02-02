package com.dbrand.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderItem {
    private UUID id;
    private UUID orderId;
    private UUID shoeId;
    private Integer size;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;

    // Constructors
    public OrderItem() {}

    public OrderItem(UUID orderId, UUID shoeId, Integer size, Integer quantity, BigDecimal price) {
        this.orderId = orderId;
        this.shoeId = shoeId;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public UUID getShoeId() { return shoeId; }
    public void setShoeId(UUID shoeId) { this.shoeId = shoeId; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
