package com.dbrand.controller;

import com.dbrand.dto.ShoeResponse;
import com.dbrand.dto.CreateShoeRequest;
import com.dbrand.service.ShoeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shoes")
@Tag(name = "Shoes", description = "Shoe management APIs")
public class ShoeController {

    @Autowired
    private ShoeService shoeService;

    @GetMapping
    @Operation(summary = "Get all shoes with optional filters")
    public ResponseEntity<List<ShoeResponse>> getAllShoes(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer minSize,
            @RequestParam(required = false) Integer maxSize,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<ShoeResponse> shoes = shoeService.getAllShoes(brand, minSize, maxSize, minPrice, maxPrice);
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shoe by ID")
    public ResponseEntity<ShoeResponse> getShoeById(@PathVariable UUID id) {
        return shoeService.getShoeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new shoe (Admin only)")
    public ResponseEntity<ShoeResponse> createShoe(@Valid @RequestBody CreateShoeRequest request) {
        ShoeResponse response = shoeService.createShoe(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a shoe (Admin only)")
    public ResponseEntity<ShoeResponse> updateShoe(@PathVariable UUID id, @Valid @RequestBody CreateShoeRequest request) {
        try {
            ShoeResponse response = shoeService.updateShoe(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a shoe (Admin only)")
    public ResponseEntity<Void> deleteShoe(@PathVariable UUID id) {
        try {
            shoeService.deleteShoe(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/sizes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update shoe size and stock (Admin only)")
    public ResponseEntity<Void> updateShoeSize(
            @PathVariable UUID id,
            @RequestParam Integer size,
            @RequestParam Integer stockCount) {
        try {
            shoeService.updateShoeSize(id, size, stockCount);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/sizes")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete shoe size (Admin only)")
    public ResponseEntity<Void> deleteShoeSize(
            @PathVariable UUID id,
            @RequestParam Integer size) {
        shoeService.deleteShoeSize(id, size);
        return ResponseEntity.ok().build();
    }
}
