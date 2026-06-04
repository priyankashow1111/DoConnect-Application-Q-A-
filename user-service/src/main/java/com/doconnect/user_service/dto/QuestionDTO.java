package com.doconnect.user_service.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Integer questionId;
    private String title;
    private String description;
    private String topic;
    private String status;
    private Boolean isApproved;
    private String askedBy;
    private Integer userId;
    private LocalDateTime createdAt;
}