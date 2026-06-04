package com.doconnect.notification_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionNotificationRequest {
    private Integer questionId;
    private String questionTitle;
    private String askedBy;
    private String topic;
}