package com.doconnect.user_service.service;

import com.doconnect.user_service.dto.*;
import com.doconnect.user_service.entity.*;
import com.doconnect.user_service.exception.*;
import com.doconnect.user_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    // Send message
    @org.springframework.transaction.annotation.Transactional
    public ChatMessageDTO sendMessage(
            ChatMessageRequest request, String senderUsername) {

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Receiver not found"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .messageText(request.getMessageText())
                .isRead(false)
                .build();

        chatMessageRepository.save(message);
        return mapToDTO(message);
    }

    // Get conversation between two users
    public List<ChatMessageDTO> getConversation(
            Integer userId1, Integer userId2) {
        return chatMessageRepository
                .findConversation(userId1, userId2)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get unread messages
    public List<ChatMessageDTO> getUnreadMessages(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        return chatMessageRepository
                .findByReceiverUserIdAndIsReadFalse(user.getUserId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Mark message as read
    public String markAsRead(Integer messageId) {
        ChatMessage message = chatMessageRepository
                .findById(messageId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Message not found"));
        message.setIsRead(true);
        chatMessageRepository.save(message);
        return "Message marked as read";
    }

    // Get unread message count (for notification badge)
    public long getUnreadCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return chatMessageRepository.countByReceiverUserIdAndIsReadFalse(user.getUserId());
    }

    // Mark all messages from a sender as read (when user opens conversation)
    @org.springframework.transaction.annotation.Transactional
    public String markConversationAsRead(Integer senderId, String receiverUsername) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        chatMessageRepository.markConversationAsRead(senderId, receiver.getUserId());
        return "Conversation marked as read";
    }

    private ChatMessageDTO mapToDTO(ChatMessage m) {
        return ChatMessageDTO.builder()
                .messageId(m.getMessageId())
                .senderId(m.getSender().getUserId())
                .senderUsername(m.getSender().getUsername())
                .receiverId(m.getReceiver().getUserId())
                .receiverUsername(m.getReceiver().getUsername())
                .messageText(m.getMessageText())
                .isRead(m.getIsRead())
                .sentAt(m.getSentAt())
                .build();
    }
}