package com.doconnect.admin_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAuthResponse {
    private String token;
    private String username;
    private String email;
    private String role;
    private Integer adminId;
    private String message;
}