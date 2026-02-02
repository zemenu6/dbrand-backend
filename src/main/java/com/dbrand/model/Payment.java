/**
 * PAYMENT MODEL - Payment entity for financial tracking
 * Purpose: Represents payment transactions with gateway integration
 * Advantages: Secure references, status tracking, audit trails
 */

package com.dbrand.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment - Domain model for financial transactions
 * Features: Gateway integration, status workflow, amount precision
 * Integration: Order processing, financial reporting
 */
public class Payment {
    // UUID primary key for security
    // Advantage: Non-sequential, secure transaction identification
    private UUID id;
    
    // Order reference for transaction linking
    // Purpose: Connect payment to specific order
    private UUID orderId;
    
    // Payment processing status
    // Values: PENDING, PAID, FAILED, REFUNDED
    private String status;
    
    // External payment gateway reference
    // Advantage: Reconciliation with payment providers
    private String transactionReference;
    
    // Payment amount with precision
    // Purpose: Accurate financial calculations
    private BigDecimal amount;
    
    // Payment method identifier
    // Advantage: Track payment channel preferences
    private String paymentMethod;
    
    // Payment completion timestamp
    // Purpose: Track payment timing
    private LocalDateTime paymentDate;
    
    // Gateway response data
    // Advantage: Store provider feedback for debugging
    private String gatewayResponse;
    
    // Creation audit timestamp
    // Purpose: Track payment initiation
    private LocalDateTime createdAt;
    
    // Update audit timestamp
    // Advantage: Track status changes
    private LocalDateTime updatedAt;

    // Default constructor
    // Required: JPA and JSON serialization
    public Payment() {}

    /**
     * Parameterized constructor
     * Purpose: Create payment with essential transaction data
     * Integration: Payment processing service
     */
    public Payment(UUID orderId, String status, BigDecimal amount) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
    }

    // Getters and setters with JavaBean pattern
    // Advantage: Framework compatibility, clean encapsulation
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
