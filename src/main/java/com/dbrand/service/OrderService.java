package com.dbrand.service;

import com.dbrand.dto.*;
import com.dbrand.model.Order;
import com.dbrand.model.OrderItem;
import com.dbrand.model.Payment;
import com.dbrand.repository.OrderItemRepository;
import com.dbrand.repository.OrderRepository;
import com.dbrand.repository.PaymentRepository;
import com.dbrand.repository.ShoeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ShoeRepository shoeRepository;

    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       PaymentRepository paymentRepository, 
                       ShoeRepository shoeRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.shoeRepository = shoeRepository;
    }

    @Transactional
    public OrderResponse createOrder(UUID userId, CreateOrderRequest request) {
        // Calculate total price and validate stock
        final BigDecimal[] totalPrice = {BigDecimal.ZERO}; // Use array to make it effectively final
        
        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    // Get shoe price
                    var shoeOpt = shoeRepository.findById(UUID.fromString(itemRequest.getShoeId()));
                    if (shoeOpt.isEmpty()) {
                        throw new IllegalArgumentException("Shoe not found: " + itemRequest.getShoeId());
                    }

                    // Check stock availability (simplified - you might want to check specific sizes)
                    var sizes = shoeRepository.findSizesByShoeId(UUID.fromString(itemRequest.getShoeId()));
                    boolean sizeAvailable = sizes.stream()
                            .anyMatch(size -> size.getSize().equals(itemRequest.getSize()) && 
                                           size.getStockCount() >= itemRequest.getQuantity());
                    
                    if (!sizeAvailable) {
                        throw new IllegalArgumentException("Insufficient stock for shoe: " + itemRequest.getShoeId() + 
                                                 ", size: " + itemRequest.getSize());
                    }

                    BigDecimal itemPrice = shoeOpt.get().getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                    totalPrice[0] = totalPrice[0].add(itemPrice);

                    return new OrderItem(
                            null, // Will be set when saved
                            UUID.fromString(itemRequest.getShoeId()),
                            itemRequest.getSize(),
                            itemRequest.getQuantity(),
                            shoeOpt.get().getPrice()
                    );
                })
                .toList();

        // Create order
        Order order = new Order(userId, totalPrice[0], "PROCESSING", request.getDeliveryDays());
        Order savedOrder = orderRepository.save(order);

        // Save order items
        orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
        orderItemRepository.saveAll(orderItems);

        // Create payment record
        Payment payment = new Payment(savedOrder.getId(), "PENDING", totalPrice[0]);
        paymentRepository.save(payment);

        // Build response
        OrderResponse response = new OrderResponse();
        response.setId(savedOrder.getId().toString());
        response.setUserId(savedOrder.getUserId().toString());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        response.setDeliveryDate(savedOrder.getDeliveryDate());
        response.setDeliveryDays(savedOrder.getDeliveryDays());

        List<OrderItemResponse> itemResponses = orderItemRepository.findByOrderId(savedOrder.getId());
        response.setItems(itemResponses);

        return response;
    }

    public List<OrderResponse> getUserOrders(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .toList();
    }

    public Optional<OrderResponse> getOrderById(UUID id) {
        return orderRepository.findById(id)
                .map(this::convertToOrderResponse);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderResponse)
                .toList();
    }

    public long getOrderCount() {
        return orderRepository.count();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrderStatus(String id, UpdateOrderStatusRequest request) {
        Order order = getOrderById(id);
        order.setStatus(request.getStatus());
        order.setDeliveryDays(request.getDeliveryDays());
        return orderRepository.save(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID id, UpdateOrderStatusRequest request) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }

        Order order = orderOpt.get();
        order.setStatus(request.getStatus());
        order.setDeliveryDays(request.getDeliveryDays());
        Order updatedOrder = orderRepository.save(order);

        // Update payment status if order is marked as shipped/delivered
        if ("SHIPPED".equals(request.getStatus()) || "DELIVERED".equals(request.getStatus())) {
            Optional<Payment> paymentOpt = paymentRepository.findByOrderId(id);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setStatus("PAID");
                payment.setPaymentDate(LocalDateTime.now());
                payment.setTransactionReference("AUTO_" + System.currentTimeMillis());
                paymentRepository.save(payment);
            }
        }

        return convertToOrderResponse(updatedOrder);
    }

    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId().toString());
        response.setUserId(order.getUserId().toString());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setDeliveryDate(order.getDeliveryDate());
        response.setDeliveryDays(order.getDeliveryDays());

        List<OrderItemResponse> items = orderItemRepository.findByOrderId(order.getId());
        response.setItems(items);

        return response;
    }
}
