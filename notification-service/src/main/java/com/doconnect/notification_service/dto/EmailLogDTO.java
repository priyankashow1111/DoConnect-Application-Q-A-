package com.doconnect.notification_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLogDTO {
    private Integer logId;
    private String recipientEmail;
    private String subject;
    private String status;
    private String notificationType;
    private String errorMessage;
    private LocalDateTime sentAt;
}