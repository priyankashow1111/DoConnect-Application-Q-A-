package com.doconnect.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @Column(nullable = false)
    private String recipientEmail;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    // SENT, FAILED
    @Builder.Default
    private String status = "SENT";

    // QUESTION_ASKED, ANSWER_POSTED
    private String notificationType;

    private String errorMessage;

    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }
}