package com.doconnect.admin_service.service;

import com.doconnect.admin_service.dto.*;
import com.doconnect.admin_service.entity.*;
import com.doconnect.admin_service.exception.*;
import com.doconnect.admin_service.repository.*;
import com.doconnect.admin_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalService {

    @Autowired
    private QuestionApprovalRepository questionApprovalRepository;

    @Autowired
    private AnswerApprovalRepository answerApprovalRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private JwtUtil jwtUtil;

    private String serviceToken() {
        return "Bearer " + jwtUtil.generateToken("admin-service");
    }

    // Approve question
    public ApprovalDTO approveQuestion(Integer questionId,
            ApprovalRequest request, String adminUsername) {

        Admin admin = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found"));

        // Call user-service to actually approve
        webClient.put()
                .uri("/api/questions/approve/" + questionId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Save approval record
        QuestionApproval approval = questionApprovalRepository
                .findByQuestionId(questionId)
                .orElse(QuestionApproval.builder()
                        .questionId(questionId)
                        .build());

        approval.setAdmin(admin);
        approval.setApprovalStatus("APPROVED");
        approval.setRemarks(request.getRemarks());
        approval.setApprovedAt(LocalDateTime.now());

        questionApprovalRepository.save(approval);

        return mapQuestionApprovalToDTO(approval);
    }

    // Reject question
    public ApprovalDTO rejectQuestion(Integer questionId,
            ApprovalRequest request, String adminUsername) {

        Admin admin = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found"));

        // Call user-service to mark question as REJECTED
        webClient.put()
                .uri("/api/questions/reject/" + questionId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        QuestionApproval approval = questionApprovalRepository
                .findByQuestionId(questionId)
                .orElse(QuestionApproval.builder()
                        .questionId(questionId)
                        .build());

        approval.setAdmin(admin);
        approval.setApprovalStatus("REJECTED");
        approval.setRemarks(request.getRemarks());
        approval.setApprovedAt(LocalDateTime.now());

        questionApprovalRepository.save(approval);
        return mapQuestionApprovalToDTO(approval);
    }

    // Approve answer
    public ApprovalDTO approveAnswer(Integer answerId,
            ApprovalRequest request, String adminUsername) {

        Admin admin = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found"));

        // Call user-service to approve answer
        webClient.put()
                .uri("/api/answers/approve/" + answerId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        AnswerApproval approval = answerApprovalRepository
                .findByAnswerId(answerId)
                .orElse(AnswerApproval.builder()
                        .answerId(answerId)
                        .build());

        approval.setAdmin(admin);
        approval.setApprovalStatus("APPROVED");
        approval.setRemarks(request.getRemarks());
        approval.setApprovedAt(LocalDateTime.now());

        answerApprovalRepository.save(approval);
        return mapAnswerApprovalToDTO(approval);
    }

    // Reject answer
    public ApprovalDTO rejectAnswer(Integer answerId,
            ApprovalRequest request, String adminUsername) {

        Admin admin = adminRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Admin not found"));

        AnswerApproval approval = answerApprovalRepository
                .findByAnswerId(answerId)
                .orElse(AnswerApproval.builder()
                        .answerId(answerId)
                        .build());

        approval.setAdmin(admin);
        approval.setApprovalStatus("REJECTED");
        approval.setRemarks(request.getRemarks());
        approval.setApprovedAt(LocalDateTime.now());

        answerApprovalRepository.save(approval);
        return mapAnswerApprovalToDTO(approval);
    }

    // Delete question (calls user-service)
    public String deleteQuestion(Integer questionId) {
        webClient.delete()
                .uri("/api/questions/delete/" + questionId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "Question deleted successfully";
    }

    // Delete answer (calls user-service)
    public String deleteAnswer(Integer answerId) {
        webClient.delete()
                .uri("/api/answers/delete/" + answerId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "Answer deleted successfully";
    }

    // Close question thread (calls user-service)
    public String closeQuestion(Integer questionId) {
        webClient.put()
                .uri("/api/questions/close/" + questionId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "Question closed and marked as resolved";
    }

    // Get all users (calls user-service)
    public Object getAllUsers() {
        return webClient.get()
                .uri("/api/users/all")
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {})
                .block();
    }

    // Deactivate user (calls user-service)
    public String deactivateUser(Integer userId) {
        webClient.put()
                .uri("/api/users/deactivate/" + userId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "User deactivated successfully";
    }

    // Activate user (calls user-service)
    public String activateUser(Integer userId) {
        webClient.put()
                .uri("/api/users/activate/" + userId)
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "User activated successfully";
    }

    // Get all unapproved questions
    public Object getUnapprovedQuestions() {
        return webClient.get()
                .uri("/api/questions/unapproved")
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
    }

    // Get all answers
    public Object getAllAnswers() {
        return webClient.get()
                .uri("/api/answers/admin/all")
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
    }

    // Get all unapproved answers
    public Object getUnapprovedAnswers() {
        return webClient.get()
                .uri("/api/answers/unapproved")
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
    }

    // Get all questions
    public Object getAllQuestions() {
        return webClient.get()
                .uri("/api/questions/admin/all")
                .header("Authorization", serviceToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {})
                .block();
    }

    private ApprovalDTO mapQuestionApprovalToDTO(
            QuestionApproval approval) {
        return ApprovalDTO.builder()
                .approvalId(approval.getApprovalId())
                .questionId(approval.getQuestionId())
                .adminId(approval.getAdmin() != null ?
                    approval.getAdmin().getAdminId() : null)
                .adminUsername(approval.getAdmin() != null ?
                    approval.getAdmin().getUsername() : null)
                .approvalStatus(approval.getApprovalStatus())
                .remarks(approval.getRemarks())
                .approvedAt(approval.getApprovedAt())
                .build();
    }

    private ApprovalDTO mapAnswerApprovalToDTO(
            AnswerApproval approval) {
        return ApprovalDTO.builder()
                .approvalId(approval.getApprovalId())
                .answerId(approval.getAnswerId())
                .adminId(approval.getAdmin() != null ?
                    approval.getAdmin().getAdminId() : null)
                .adminUsername(approval.getAdmin() != null ?
                    approval.getAdmin().getUsername() : null)
                .approvalStatus(approval.getApprovalStatus())
                .remarks(approval.getRemarks())
                .approvedAt(approval.getApprovedAt())
                .build();
    }
}
