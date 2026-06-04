package com.doconnect.user_service.controller;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Add comment
    @PostMapping("/add/{answerId}")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Integer answerId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.addComment(
            answerId, request, userDetails.getUsername()));
    }

    // Get comments for answer
    @GetMapping("/answer/{answerId}")
    public ResponseEntity<List<CommentDTO>> getComments(
            @PathVariable Integer answerId) {
        return ResponseEntity.ok(
            commentService.getCommentsForAnswer(answerId));
    }

    // Delete comment
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Integer commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(commentService.deleteComment(
            commentId, userDetails.getUsername()));
    }
}