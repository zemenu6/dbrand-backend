/**
 * USER MODEL - User entity for authentication and authorization
 * Purpose: Represents user accounts with role-based access control
 * Advantages: Secure UUID keys, JSON serialization, audit trails
 */

package com.dbrand.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    
    // Hashed password for security
    // Advantage: Never store plain text passwords
    private String passwordHash;
    
    private String role;
    
    // Account status flag with JSON mapping
    // Purpose: Enable/disable accounts without deletion
    @JsonProperty("isEnabled")
    private boolean isEnabled;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public User(String name, String email, String phone, String passwordHash, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isEnabled = true;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }
    
    // Audit trail timestamps for tracking user lifecycle
    // Advantage: Complete user activity monitoring
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
