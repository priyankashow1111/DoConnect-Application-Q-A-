package com.doconnect.user_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doconnect.user_service.dto.AuthResponse;
import com.doconnect.user_service.dto.LoginRequest;
import com.doconnect.user_service.dto.RegisterRequest;
import com.doconnect.user_service.dto.UserDTO;
import com.doconnect.user_service.entity.User;
import com.doconnect.user_service.exception.InvalidCredentialsException;
import com.doconnect.user_service.exception.ResourceNotFoundException;
import com.doconnect.user_service.exception.UserAlreadyExistsException;
import com.doconnect.user_service.repository.UserRepository;
import com.doconnect.user_service.security.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("Username already taken");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException("Email already registered");

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .isActive(true)
                .role("USER")
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getUserId())
                .message("Registration successful")
                .build();
    }

    // Login
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                    new InvalidCredentialsException("Invalid username or password"));

        if (!user.getIsActive())
            throw new InvalidCredentialsException("Account is deactivated");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid username or password");

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getUserId())
                .message("Login successful")
                .build();
    }

    // Get all users
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found with id: " + userId));
        return mapToDTO(user);
    }

    // Get user by username
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found: " + username));
        return mapToDTO(user);
    }

    // Update user
    public UserDTO updateUser(Integer userId, RegisterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return mapToDTO(user);
    }

    // Deactivate user (admin uses this)
    public String deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
        return "User deactivated successfully";
    }

    // Activate user
    public String activateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));
        user.setIsActive(true);
        userRepository.save(user);
        return "User activated successfully";
    }

    // Map entity to DTO
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePic(user.getProfilePic())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .build();
    }
}