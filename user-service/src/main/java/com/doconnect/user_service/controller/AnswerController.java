package com.doconnect.user_service.controller;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Post answer
    @PostMapping("/post/{questionId}")
    public ResponseEntity<AnswerDTO> postAnswer(
            @PathVariable Integer questionId,
            @Valid @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(answerService.postAnswer(
            questionId, request, userDetails.getUsername()));
    }

    // Get current user's pending answer for a question
    @GetMapping("/my-pending/{questionId}")
    public ResponseEntity<AnswerDTO> getMyPendingAnswer(
            @PathVariable Integer questionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        AnswerDTO pending = answerService.getMyPendingAnswer(
            questionId, userDetails.getUsername());
        return pending != null
            ? ResponseEntity.ok(pending)
            : ResponseEntity.noContent().build();
    }

    // Get approved answers for question
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerDTO>> getApprovedAnswers(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            answerService.getApprovedAnswers(questionId));
    }

    // Get all answers for question (admin)
    @GetMapping("/admin/question/{questionId}")
    public ResponseEntity<List<AnswerDTO>> getAllAnswers(
            @PathVariable Integer questionId) {
        return ResponseEntity.ok(
            answerService.getAllAnswersForQuestion(questionId));
    }

    // Get all answers (admin)
    @GetMapping("/admin/all")
    public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    // Get unapproved answers (admin)
    @GetMapping("/unapproved")
    public ResponseEntity<List<AnswerDTO>> getUnapproved() {
        return ResponseEntity.ok(answerService.getUnapprovedAnswers());
    }

    // Approve answer (admin)
    @PutMapping("/approve/{answerId}")
    public ResponseEntity<String> approveAnswer(
            @PathVariable Integer answerId) {
        return ResponseEntity.ok(answerService.approveAnswer(answerId));
    }

    // Delete answer
    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<String> deleteAnswer(
            @PathVariable Integer answerId) {
        return ResponseEntity.ok(answerService.deleteAnswer(answerId));
    }

    // Update answer
    @PutMapping("/update/{answerId}")
    public ResponseEntity<AnswerDTO> updateAnswer(
            @PathVariable Integer answerId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(answerService.updateAnswer(
            answerId, request, userDetails.getUsername()));
    }
}