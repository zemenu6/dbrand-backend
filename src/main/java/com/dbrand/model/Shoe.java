/**
 * SHOE MODEL - Product entity for inventory management
 * Purpose: Represents shoe products with status tracking
 * Advantages: Active/inactive states, audit trails, UUID security
 */

package com.dbrand.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Shoe - Domain model for product catalog
 * Features: Price management, status control, brand organization
 * Integration: Inventory service, order processing
 */
public class Shoe {
    // UUID primary key for security
    // Advantage: Non-sequential, secure product identification
    private UUID id;
    
    // Product display name
    // Purpose: Customer-facing product title
    private String name;
    
    // Brand classification
    // Advantage: Organized product categorization
    private String brand;
    
    // Detailed product information
    // Purpose: Rich content for customer decisions
    private String description;
    
    // Product pricing with precision
    // Advantage: Accurate financial calculations
    private BigDecimal price;
    
    // Product image URL
    // Purpose: Visual representation for customers
    private String imageUrl;
    
    // Product availability status
    // Purpose: Control product visibility without deletion
    private boolean isActive;
    
    // Creation audit timestamp
    // Advantage: Track product lifecycle
    private LocalDateTime createdAt;
    
    // Update audit timestamp
    // Purpose: Track last modification
    private LocalDateTime updatedAt;

    // Default constructor
    // Required: JPA and JSON serialization
    public Shoe() {}

    /**
     * Parameterized constructor
     * Purpose: Create shoe with essential product data
     * Default: Sets isActive to true for new products
     */
    public Shoe(String name, String brand, String description, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.isActive = true;
    }

    // Getters and setters with JavaBean pattern
    // Advantage: Framework compatibility, clean encapsulation
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
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
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
