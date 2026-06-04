package com.doconnect.admin_service.controller;

import com.doconnect.admin_service.dto.*;
import com.doconnect.admin_service.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    // ===== USER MANAGEMENT =====

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(approvalService.getAllUsers());
    }

    @PutMapping("/users/deactivate/{userId}")
    public ResponseEntity<String> deactivateUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(
            approvalService.deactivateUser(userId));
    }

    @PutMapping("/users/activate/{userId}")
    public ResponseEntity<String> activateUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(
            approvalService.activateUser(userId));
    }

    // ===== QUESTION MANAGEMENT =====

    @GetMapping("/questions")
    public ResponseEntity<Object> getAllQuestions() {
        return ResponseEntity.ok(approvalService.getAllQuestions());
    }

    @GetMapping("/questions/unapproved")
    public ResponseEntity<Object> getUnapprovedQuestions() {
        return ResponseEntity.ok(
            approvalService.getUnapprovedQuestions());
    }

    @PutMapping("/questions/approve/{questionId}")
    public ResponseEntity<ApprovalDTO> approveQuestion(
            @PathVariable Integer questionId,
            @RequestBody ApprovalRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(approvalService.approveQuestion(
            questionId, request, userDetails.getUsername()));
    }

    @PutMapping("/questions/reject/{questionId}")
    public ResponseEntity<ApprovalDTO> rejectQuestion(
            @PathVariable Integer questionId,
            @RequestBody ApprovalRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(approvalService.rejectQuestion(
            questionId, request, userDetails.getUsername()));
    }

    @DeleteMapping("/questions/delete/{questionId}")
    public ResponseEntity<String> deleteQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            approvalService.deleteQuestion(questionId));
    }

    @PutMapping("/questions/close/{questionId}")
    public ResponseEntity<String> closeQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            approvalService.closeQuestion(questionId));
    }

    // ===== ANSWER MANAGEMENT =====

    @GetMapping("/answers/unapproved")
    public ResponseEntity<Object> getUnapprovedAnswers() {
        return ResponseEntity.ok(
            approvalService.getUnapprovedAnswers());
    }

    @GetMapping("/answers/all")
    public ResponseEntity<Object> getAllAnswers() {
        return ResponseEntity.ok(
            approvalService.getAllAnswers());
    }

    @PutMapping("/answers/approve/{answerId}")
    public ResponseEntity<ApprovalDTO> approveAnswer(
            @PathVariable Integer answerId,
            @RequestBody ApprovalRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(approvalService.approveAnswer(
            answerId, request, userDetails.getUsername()));
    }

    @PutMapping("/answers/reject/{answerId}")
    public ResponseEntity<ApprovalDTO> rejectAnswer(
            @PathVariable Integer answerId,
            @RequestBody ApprovalRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(approvalService.rejectAnswer(
            answerId, request, userDetails.getUsername()));
    }

    @DeleteMapping("/answers/delete/{answerId}")
    public ResponseEntity<String> deleteAnswer(
            @PathVariable Integer answerId) {
        return ResponseEntity.ok(
            approvalService.deleteAnswer(answerId));
    }
}