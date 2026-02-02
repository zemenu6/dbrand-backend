package com.dbrand.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class UpdateOrderStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotNull(message = "Delivery days is required")
    @Min(value = 0, message = "Delivery days must be at least 0")
    private Integer deliveryDays;

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(Integer deliveryDays) { this.deliveryDays = deliveryDays; }
}
