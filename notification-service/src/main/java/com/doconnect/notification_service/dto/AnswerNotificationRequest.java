package com.doconnect.notification_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerNotificationRequest {
    private Integer answerId;
    private Integer questionId;
    private String questionTitle;
    private String answeredBy;
    private String answerText;
}