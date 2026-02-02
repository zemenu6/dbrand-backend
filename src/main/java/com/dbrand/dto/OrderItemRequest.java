package com.dbrand.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderItemRequest {
    @NotBlank(message = "Shoe ID is required")
    private String shoeId;
    
    @NotNull(message = "Size is required")
    @Min(value = 1, message = "Size must be at least 1")
    private Integer size;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Getters and setters
    public String getShoeId() { return shoeId; }
    public void setShoeId(String shoeId) { this.shoeId = shoeId; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
