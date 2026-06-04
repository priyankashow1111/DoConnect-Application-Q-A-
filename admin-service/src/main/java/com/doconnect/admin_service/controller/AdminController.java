package com.doconnect.admin_service.controller;

import com.doconnect.admin_service.dto.*;
import com.doconnect.admin_service.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Register admin
    @PostMapping("/register")
    public ResponseEntity<AdminAuthResponse> register(
            @Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.register(request));
    }

    // Login admin
    @PostMapping("/login")
    public ResponseEntity<AdminAuthResponse> login(
            @Valid @RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }

    // Get all admins
    @GetMapping("/all")
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // Get admin by ID
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTO> getAdminById(
            @PathVariable Integer adminId) {
        return ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    // Update admin
    @PutMapping("/update/{adminId}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable Integer adminId,
            @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.updateAdmin(adminId, request));
    }

    // Delete admin
    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<String> deleteAdmin(
            @PathVariable Integer adminId) {
        return ResponseEntity.ok(adminService.deleteAdmin(adminId));
    }
}