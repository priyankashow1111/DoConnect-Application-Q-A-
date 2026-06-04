package com.doconnect.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDTO {

    private Integer answerId;
    private String answerText;
    private Boolean isApproved;
    private String answeredBy;
    private Integer userId;
    private Integer questionId;
    private Long likeCount;
    private LocalDateTime createdAt;
}