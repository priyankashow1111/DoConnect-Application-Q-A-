package com.doconnect.admin_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private Integer adminId;
    private String username;
    private String email;
    private String fullName;
    private Boolean isActive;
    private String role;
}