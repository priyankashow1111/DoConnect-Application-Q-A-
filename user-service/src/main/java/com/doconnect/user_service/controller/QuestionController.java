package com.doconnect.user_service.controller;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Ask question
    @PostMapping("/ask")
    public ResponseEntity<QuestionDTO> askQuestion(
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            questionService.askQuestion(request, userDetails.getUsername()));
    }

    // Get all approved questions
    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllApprovedQuestions());
    }

    // Get all questions including unapproved (admin)
    @GetMapping("/admin/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsAdmin() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    // Search questions
    @GetMapping("/search")
    public ResponseEntity<List<QuestionDTO>> searchQuestions(
            @RequestParam String keyword) {
        return ResponseEntity.ok(questionService.searchQuestions(keyword));
    }

    // Get question by ID
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            questionService.getQuestionById(questionId));
    }

    // Get questions by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuestionDTO>> getByUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(
            questionService.getQuestionsByUser(userId));
    }

    // Get my questions with status (authenticated user)
    @GetMapping("/my-questions")
    public ResponseEntity<List<QuestionDTO>> getMyQuestions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            questionService.getMyQuestions(userDetails.getUsername()));
    }

    // Update question
    @PutMapping("/update/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Integer questionId,
            @RequestBody QuestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(questionService.updateQuestion(
            questionId, request, userDetails.getUsername()));
    }

    // Delete question
    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<String> deleteQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            questionService.deleteQuestion(questionId));
    }

    // Approve question (admin calls this)
    @PutMapping("/approve/{questionId}")
    public ResponseEntity<String> approveQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            questionService.approveQuestion(questionId));
    }

    // Reject question (admin calls this)
    @PutMapping("/reject/{questionId}")
    public ResponseEntity<String> rejectQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            questionService.rejectQuestion(questionId));
    }

    // Close question (admin)
    @PutMapping("/close/{questionId}")
    public ResponseEntity<String> closeQuestion(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            questionService.closeQuestion(questionId));
    }

    // Get unapproved questions (admin)
    @GetMapping("/unapproved")
    public ResponseEntity<List<QuestionDTO>> getUnapproved() {
        return ResponseEntity.ok(questionService.getUnapprovedQuestions());
    }
}