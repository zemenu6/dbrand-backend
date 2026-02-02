package com.dbrand.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class ShoeResponse {
    private String id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    
    @JsonProperty("image_url")
    private String imageUrl;
    
    private List<ShoeSizeResponse> sizes;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public List<ShoeSizeResponse> getSizes() { return sizes; }
    public void setSizes(List<ShoeSizeResponse> sizes) { this.sizes = sizes; }
}
