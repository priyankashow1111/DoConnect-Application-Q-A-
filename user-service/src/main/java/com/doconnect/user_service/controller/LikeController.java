package com.doconnect.user_service.controller;


import com.doconnect.user_service.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Toggle like
    @PostMapping("/toggle/{answerId}")
    public ResponseEntity<String> toggleLike(
            @PathVariable Integer answerId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            likeService.toggleLike(answerId, userDetails.getUsername()));
    }

    // Get like count
    @GetMapping("/count/{answerId}")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Integer answerId) {
        return ResponseEntity.ok(likeService.getLikeCount(answerId));
    }

    // Check if user liked
    @GetMapping("/check/{answerId}")
    public ResponseEntity<Boolean> hasUserLiked(
            @PathVariable Integer answerId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            likeService.hasUserLiked(answerId, userDetails.getUsername()));
    }
}