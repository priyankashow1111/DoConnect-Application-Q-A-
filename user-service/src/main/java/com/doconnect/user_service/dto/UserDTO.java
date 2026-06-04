package com.doconnect.user_service.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private String profilePic;
    private Boolean isActive;
    private String role;
}