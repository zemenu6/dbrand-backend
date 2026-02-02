/**
 * AUTHENTICATION CONTROLLER - Backend API Authentication Endpoints
 * Purpose: Handles user authentication requests for Dbrand Shoes e-commerce
 * Advantages: RESTful design, JWT security, auto-generated docs
 */

package com.dbrand.controller;

import com.dbrand.dto.AuthResponse;
import com.dbrand.dto.LoginRequest;
import com.dbrand.dto.SignupRequest;
import com.dbrand.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    // AuthService dependency for business logic
    // Advantage: Separation of concerns, testable code
    @Autowired
    private AuthService authService;

    /**
     * User Registration Endpoint
     * Purpose: Creates new user account with validation
     * Advantages: Auto validation, secure hashing, JWT generation
     */
    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Secure credential verification with JWT token generation
        // Advantage: Role-based access control and session management
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
