package com.dbrand.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OrderItemResponse {
    @JsonProperty("shoe_id")
    private String shoeId;
    
    @JsonProperty("shoe_name")
    private String shoeName;
    
    @JsonProperty("shoe_brand")
    private String shoeBrand;
    
    private Integer size;
    private Integer quantity;
    private BigDecimal price;
    
    @JsonProperty("image_url")
    private String imageUrl;

    // Getters and setters
    public String getShoeId() { return shoeId; }
    public void setShoeId(String shoeId) { this.shoeId = shoeId; }
    
    public String getShoeName() { return shoeName; }
    public void setShoeName(String shoeName) { this.shoeName = shoeName; }
    
    public String getShoeBrand() { return shoeBrand; }
    public void setShoeBrand(String shoeBrand) { this.shoeBrand = shoeBrand; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
