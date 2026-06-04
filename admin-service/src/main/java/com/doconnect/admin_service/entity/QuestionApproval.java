package com.doconnect.admin_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_approval")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer approvalId;

    // This is question ID from user-service
    @Column(nullable = false)
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // PENDING, APPROVED, REJECTED
    @Builder.Default
    private String approvalStatus = "PENDING";

    private String remarks;

    private LocalDateTime approvedAt;
}