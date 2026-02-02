package com.dbrand.controller;

import com.dbrand.dto.*;
import com.dbrand.service.AuthService;
import com.dbrand.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthService authService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        var currentUser = authService.getCurrentUser();
        OrderResponse response = orderService.createOrder(currentUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        var currentUser = authService.getCurrentUser();
        List<OrderResponse> orders = orderService.getUserOrders(currentUser.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        var currentUser = authService.getCurrentUser();
        Optional<OrderResponse> orderOpt = orderService.getOrderById(id);
        
        if (orderOpt.isPresent()) {
            OrderResponse order = orderOpt.get();
            // Check if user owns this order or is admin
            if (order.getUserId().equals(currentUser.getId().toString()) || 
                "ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.ok(order);
            }
        }
        
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        try {
            OrderResponse response = orderService.updateOrderStatus(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
