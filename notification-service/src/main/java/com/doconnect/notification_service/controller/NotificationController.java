package com.doconnect.notification_service.controller;

import com.doconnect.notification_service.dto.*;
import com.doconnect.notification_service.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    // Send custom email
    @PostMapping("/send-email")
    public ResponseEntity<EmailLogDTO> sendEmail(
            @Valid @RequestBody EmailRequest request) {
        return ResponseEntity.ok(emailService.sendEmail(request));
    }

    // Notify admin about new question
    @PostMapping("/question-asked")
    public ResponseEntity<EmailLogDTO> notifyNewQuestion(
            @RequestBody QuestionNotificationRequest request) {
        return ResponseEntity.ok(
            emailService.notifyNewQuestion(request));
    }

    // Notify admin about new answer
    @PostMapping("/answer-posted")
    public ResponseEntity<EmailLogDTO> notifyNewAnswer(
            @RequestBody AnswerNotificationRequest request) {
        return ResponseEntity.ok(
            emailService.notifyNewAnswer(request));
    }

    // Get all email logs
    @GetMapping("/logs")
    public ResponseEntity<List<EmailLogDTO>> getAllLogs() {
        return ResponseEntity.ok(emailService.getAllLogs());
    }

    // Get failed emails
    @GetMapping("/logs/failed")
    public ResponseEntity<List<EmailLogDTO>> getFailedEmails() {
        return ResponseEntity.ok(emailService.getFailedEmails());
    }

    // Get logs by type
    @GetMapping("/logs/type/{type}")
    public ResponseEntity<List<EmailLogDTO>> getByType(
            @PathVariable String type) {
        return ResponseEntity.ok(emailService.getLogsByType(type));
    }
}