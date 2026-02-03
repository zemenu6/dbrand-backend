package com.dbrand.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @GetMapping
    public ResponseEntity<?> getCart() {
        Map<String, Object> response = new HashMap<>();
        response.put("items", Collections.emptyList());
        response.put("total", 0.0);
        response.put("count", 0);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> item) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item added to cart");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String itemId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable String itemId, @RequestBody Map<String, Object> item) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart item updated");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
