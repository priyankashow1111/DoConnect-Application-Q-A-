package com.doconnect.notification_service.service;

import com.doconnect.notification_service.dto.*;
import com.doconnect.notification_service.entity.EmailLog;
import com.doconnect.notification_service.repository.EmailLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Value("${admin.email}")
    private String adminEmail;

    // Send custom email
    public EmailLogDTO sendEmail(EmailRequest request) {
        EmailLog log = EmailLog.builder()
                .recipientEmail(request.getRecipientEmail())
                .subject(request.getSubject())
                .body(request.getBody())
                .notificationType(request.getNotificationType())
                .status("SENT")
                .build();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getRecipientEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());
            mailSender.send(message);
            log.setStatus("SENT");
        } catch (Exception e) {
            log.setStatus("FAILED");
            log.setErrorMessage(e.getMessage());
        }
        emailLogRepository.save(log);
        return mapToDTO(log);
    }

    // Notify admin — new question asked
    public EmailLogDTO notifyNewQuestion(
            QuestionNotificationRequest request) {

        String subject = "DoConnect - New Question Asked";
        String body = "Hello Admin,\n\n" +
                "A new question has been asked on DoConnect.\n\n" +
                "Question ID : " + request.getQuestionId() + "\n" +
                "Title       : " + request.getQuestionTitle() + "\n" +
                "Topic       : " + request.getTopic() + "\n" +
                "Asked By    : " + request.getAskedBy() + "\n\n" +
                "Please login to DoConnect admin panel to review " +
                "and approve this question.\n\n" +
                "Regards,\nDoConnect System";

        EmailLog log = EmailLog.builder()
                .recipientEmail(adminEmail)
                .subject(subject)
                .body(body)
                .notificationType("QUESTION_ASKED")
                .status("SENT")
                .build();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.setStatus("SENT");
        } catch (Exception e) {
            log.setStatus("FAILED");
            log.setErrorMessage(e.getMessage());
        }

        emailLogRepository.save(log);
        return mapToDTO(log);
    }

    // Notify admin — new answer posted
    public EmailLogDTO notifyNewAnswer(
            AnswerNotificationRequest request) {

        String subject = "DoConnect - New Answer Posted";
        String body = "Hello Admin,\n\n" +
                "A new answer has been posted on DoConnect.\n\n" +
                "Answer ID      : " + request.getAnswerId() + "\n" +
                "Question ID    : " + request.getQuestionId() + "\n" +
                "Question Title : " + request.getQuestionTitle() + "\n" +
                "Answered By    : " + request.getAnsweredBy() + "\n" +
                "Answer Preview : " +
                request.getAnswerText().substring(0,
                    Math.min(100, request.getAnswerText().length()))
                + "...\n\n" +
                "Please login to DoConnect admin panel to review " +
                "and approve this answer.\n\n" +
                "Regards,\nDoConnect System";

        EmailLog log = EmailLog.builder()
                .recipientEmail(adminEmail)
                .subject(subject)
                .body(body)
                .notificationType("ANSWER_POSTED")
                .status("SENT")
                .build();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.setStatus("SENT");
        } catch (Exception e) {
            log.setStatus("FAILED");
            log.setErrorMessage(e.getMessage());
        }

        emailLogRepository.save(log);
        return mapToDTO(log);
    }

    // Get all email logs
    public List<EmailLogDTO> getAllLogs() {
        return emailLogRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get failed emails
    public List<EmailLogDTO> getFailedEmails() {
        return emailLogRepository.findByStatus("FAILED")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get logs by type
    public List<EmailLogDTO> getLogsByType(String type) {
        return emailLogRepository.findByNotificationType(type)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EmailLogDTO mapToDTO(EmailLog log) {
        return EmailLogDTO.builder()
                .logId(log.getLogId())
                .recipientEmail(log.getRecipientEmail())
                .subject(log.getSubject())
                .status(log.getStatus())
                .notificationType(log.getNotificationType())
                .errorMessage(log.getErrorMessage())
                .sentAt(log.getSentAt())
                .build();
    }
}