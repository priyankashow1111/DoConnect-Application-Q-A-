package com.doconnect.admin_service.service;

import com.doconnect.admin_service.dto.*;
import com.doconnect.admin_service.entity.Admin;
import com.doconnect.admin_service.exception.*;
import com.doconnect.admin_service.repository.AdminRepository;
import com.doconnect.admin_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register admin
    public AdminAuthResponse register(AdminRegisterRequest request) {
        if (adminRepository.existsByUsername(request.getUsername()))
            throw new AdminAlreadyExistsException(
                "Username already taken");

        if (adminRepository.existsByEmail(request.getEmail()))
            throw new AdminAlreadyExistsException(
                "Email already registered");

        Admin admin = Admin.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .isActive(true)
                .role("ADMIN")
                .build();

        adminRepository.save(admin);
        String token = jwtUtil.generateToken(admin.getUsername());

        return AdminAuthResponse.builder()
                .token(token)
                .username(admin.getUsername())
                .email(admin.getEmail())
                .role(admin.getRole())
                .adminId(admin.getAdminId())
                .message("Admin registered successfully")
                .build();
    }

    // Login admin
    public AdminAuthResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(
                    "Invalid username or password"));

        if (!admin.getIsActive())
            throw new InvalidCredentialsException(
                "Admin account is deactivated");

        if (!passwordEncoder.matches(
                request.getPassword(), admin.getPassword()))
            throw new InvalidCredentialsException(
                "Invalid username or password");

        String token = jwtUtil.generateToken(admin.getUsername());

        return AdminAuthResponse.builder()
                .token(token)
                .username(admin.getUsername())
                .email(admin.getEmail())
                .role(admin.getRole())
                .adminId(admin.getAdminId())
                .message("Login successful")
                .build();
    }

    // Get all admins
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get admin by ID
    public AdminDTO getAdminById(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found with id: " + adminId));
        return mapToDTO(admin);
    }

    // Update admin
    public AdminDTO updateAdmin(Integer adminId, AdminRegisterRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found with id: " + adminId));

        admin.setFullName(request.getFullName());
        admin.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isEmpty())
            admin.setPassword(passwordEncoder.encode(request.getPassword()));

        adminRepository.save(admin);
        return mapToDTO(admin);
    }

    // Delete admin
    public String deleteAdmin(Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found with id: " + adminId));
        adminRepository.delete(admin);
        return "Admin deleted successfully";
    }

    private AdminDTO mapToDTO(Admin admin) {
        return AdminDTO.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .isActive(admin.getIsActive())
                .role(admin.getRole())
                .build();
    }
}