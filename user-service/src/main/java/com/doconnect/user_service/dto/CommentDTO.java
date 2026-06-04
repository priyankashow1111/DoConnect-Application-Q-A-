package com.doconnect.user_service.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Integer commentId;
    private String commentText;
    private String commentedBy;
    private Integer userId;
    private Integer answerId;
    private LocalDateTime createdAt;
}