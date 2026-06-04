package com.doconnect.user_service.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Integer messageId;
    private Integer senderId;
    private String senderUsername;
    private Integer receiverId;
    private String receiverUsername;
    private String messageText;
    private Boolean isRead;
    private LocalDateTime sentAt;
}