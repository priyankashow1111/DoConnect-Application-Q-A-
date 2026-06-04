package com.doconnect.user_service.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 10, message = "Title must be at least 10 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String topic;
}