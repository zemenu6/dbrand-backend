/**
 * ADMIN CONTROLLER - Backend API Admin Management Endpoints
 * 
 * Purpose: Provides comprehensive admin management functionality for the Dbrand system
 * 
 * Key Features:
 * - User management (CRUD operations)
 * - Shoe inventory management
 * - Order processing and tracking
 * - Payment monitoring and control
 * - Dashboard statistics and analytics
 * 
 * Security:
 * - Role-based access control (ADMIN only)
 * - JWT token validation
 * - Method-level security annotations
 * 
 * Integration Points:
 * - Admin dashboard frontend
 * - Database operations via services
 * - Real-time data for management decisions
 * 
 * Advantages:
 * - Complete admin functionality in one controller
 * - Consistent API design patterns
 * - Comprehensive error handling
 * - Swagger documentation for all endpoints
 * - Scalable architecture with service layer separation
 */

package com.dbrand.controller;

import com.dbrand.dto.UpdateOrderStatusRequest;
import com.dbrand.model.Order;
import com.dbrand.model.Payment;
import com.dbrand.model.Shoe;
import com.dbrand.model.User;
import com.dbrand.service.OrderService;
import com.dbrand.service.PaymentService;
import com.dbrand.service.ShoeService;
import com.dbrand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * AdminController - REST controller for administrative operations
 * 
 * Security: All endpoints require ADMIN role
 * 
 * Advantages:
 * - Centralized admin functionality
 * - Role-based security enforcement
 * - Comprehensive business operations
 * - Real-time data access
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin management APIs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    /// Service dependencies for business logic
    /// Advantage: Clean separation of concerns
    @Autowired
    private UserService userService;

    @Autowired
    private ShoeService shoeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    // ========== USER MANAGEMENT ==========
    
    /**
     * Get All Users
     * 
     * Purpose: Retrieves complete user list for admin management
     * Advantage: Full user visibility for administrative decisions
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get User Count
     * 
     * Purpose: Provides user statistics for dashboard
     * Advantage: Quick metrics for business intelligence
     */
    @GetMapping("/users/count")
    @Operation(summary = "Get user count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        return ResponseEntity.ok(Map.of("count", userService.getUserCount()));
    }

    /**
     * Get User by ID
     * 
     * Purpose: Retrieves specific user details for editing
     * Advantage: Detailed user information for management
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Update User
     * 
     * Purpose: Modifies user information and permissions
     * Advantage: Real-time user management capabilities
     */
    @PutMapping("/users/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Delete User
     * 
     * Purpose: Removes user from system
     * Advantage: Complete user lifecycle management
     */
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // ========== SHOE MANAGEMENT ==========
    
    /**
     * Get All Shoes (Admin View)
     * 
     * Purpose: Retrieves complete shoe inventory including inactive items
     * Advantage: Full inventory visibility for management
     */
    @GetMapping("/shoes")
    @Operation(summary = "Get all shoes (admin)")
    public ResponseEntity<List<Shoe>> getAllShoes() {
        return ResponseEntity.ok(shoeService.getAllShoesAdmin());
    }

    /**
     * Get Shoe Count
     * 
     * Purpose: Provides inventory statistics for dashboard
     * Advantage: Quick inventory metrics
     */
    @GetMapping("/shoes/count")
    @Operation(summary = "Get shoe count")
    public ResponseEntity<Map<String, Long>> getShoeCount() {
        return ResponseEntity.ok(Map.of("count", shoeService.getShoeCount()));
    }

    /**
     * Get Shoe by ID (Admin View)
     * 
     * Purpose: Retrieves specific shoe details for editing
     * Advantage: Detailed product information for management
     */
    @GetMapping("/shoes/{id}")
    @Operation(summary = "Get shoe by ID (admin)")
    public ResponseEntity<Shoe> getShoeById(@PathVariable String id) {
        return ResponseEntity.ok(shoeService.getShoeByIdAdmin(id));
    }

    /**
     * Delete Shoe
     * 
     * Purpose: Removes shoe from inventory
     * Advantage: Complete inventory lifecycle management
     */
    @DeleteMapping("/shoes/{id}")
    @Operation(summary = "Delete shoe")
    public ResponseEntity<Void> deleteShoe(@PathVariable String id) {
        shoeService.deleteShoe(id);
        return ResponseEntity.ok().build();
    }

    // ========== ORDER MANAGEMENT ==========
    
    /**
     * Get All Orders
     * 
     * Purpose: Retrieves complete order list for processing
     * Advantage: Full order visibility for fulfillment
     */
    @GetMapping("/orders")
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Get Order Count
     * 
     * Purpose: Provides order statistics for dashboard
     * Advantage: Quick business metrics
     */
    @GetMapping("/orders/count")
    @Operation(summary = "Get order count")
    public ResponseEntity<Map<String, Long>> getOrderCount() {
        return ResponseEntity.ok(Map.of("count", orderService.getOrderCount()));
    }

    /**
     * Get Order by ID
     * 
     * Purpose: Retrieves specific order details for processing
     * Advantage: Detailed order information for fulfillment
     */
    @GetMapping("/orders/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Update Order Status
     * 
     * Purpose: Modifies order status and delivery information
     * Advantage: Real-time order processing capabilities
     */
    @PutMapping("/orders/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }

    // ========== PAYMENT MANAGEMENT ==========
    
    /**
     * Get All Payments
     * 
     * Purpose: Retrieves complete payment history for monitoring
     * Advantage: Full financial visibility
     */
    @GetMapping("/payments")
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /**
     * Get Total Revenue
     * 
     * Purpose: Provides revenue statistics for dashboard
     * Advantage: Key financial metrics for business decisions
     */
    @GetMapping("/payments/total")
    @Operation(summary = "Get total revenue")
    public ResponseEntity<Map<String, BigDecimal>> getTotalRevenue() {
        return ResponseEntity.ok(Map.of("total", paymentService.getTotalRevenue()));
    }

    /**
     * Get Payment by ID
     * 
     * Purpose: Retrieves specific payment details for review
     * Advantage: Detailed financial transaction information
     */
    @GetMapping("/payments/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
}