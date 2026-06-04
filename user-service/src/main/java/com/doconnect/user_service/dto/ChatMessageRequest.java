package com.doconnect.user_service.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    @NotNull(message = "Receiver ID is required")
    private Integer receiverId;

    @NotBlank(message = "Message cannot be empty")
    private String messageText;
}