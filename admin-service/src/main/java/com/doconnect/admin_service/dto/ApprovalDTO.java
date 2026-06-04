package com.doconnect.admin_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDTO {
    private Integer approvalId;
    private Integer questionId;
    private Integer answerId;
    private Integer adminId;
    private String adminUsername;
    private String approvalStatus;
    private String remarks;
    private LocalDateTime approvedAt;
}