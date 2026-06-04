package com.doconnect.user_service.controller;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Send message
    @PostMapping("/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @Valid @RequestBody ChatMessageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            chatService.sendMessage(request, userDetails.getUsername()));
    }

    // Get conversation
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<ChatMessageDTO>> getConversation(
            @PathVariable Integer userId1,
            @PathVariable Integer userId2) {
        return ResponseEntity.ok(
            chatService.getConversation(userId1, userId2));
    }

    // Get unread messages
    @GetMapping("/unread")
    public ResponseEntity<List<ChatMessageDTO>> getUnread(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            chatService.getUnreadMessages(userDetails.getUsername()));
    }

    // Get unread message count (notification badge)
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            chatService.getUnreadCount(userDetails.getUsername()));
    }

    // Mark all messages from a sender as read (user opened the conversation)
    @PutMapping("/read/conversation/{senderId}")
    public ResponseEntity<String> markConversationAsRead(
            @PathVariable Integer senderId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            chatService.markConversationAsRead(senderId, userDetails.getUsername()));
    }

    // Mark as read
    @PutMapping("/read/{messageId}")
    public ResponseEntity<String> markAsRead(
            @PathVariable Integer messageId) {
        return ResponseEntity.ok(chatService.markAsRead(messageId));
    }
}