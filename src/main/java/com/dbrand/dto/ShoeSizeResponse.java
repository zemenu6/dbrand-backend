package com.dbrand.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoeSizeResponse {
    private Integer size;
    
    @JsonProperty("stock_count")
    private Integer stockCount;

    public ShoeSizeResponse(Integer size, Integer stockCount) {
        this.size = size;
        this.stockCount = stockCount;
    }

    // Getters and setters
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Integer getStockCount() { return stockCount; }
    public void setStockCount(Integer stockCount) { this.stockCount = stockCount; }
}
