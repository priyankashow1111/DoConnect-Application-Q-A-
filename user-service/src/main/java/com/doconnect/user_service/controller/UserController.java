package com.doconnect.user_service.controller;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // Get my profile
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            userService.getUserByUsername(userDetails.getUsername()));
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Update user
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer userId,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    // Deactivate user
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<String> deactivateUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }

    // Activate user
    @PutMapping("/activate/{userId}")
    public ResponseEntity<String> activateUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(userService.activateUser(userId));
    }
}