package com.doconnect.user_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.doconnect.user_service.entity.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository
    extends JpaRepository<ChatMessage, Integer> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.sender.userId = :userId1 AND m.receiver.userId = :userId2) " +
           "OR (m.sender.userId = :userId2 AND m.receiver.userId = :userId1) " +
           "ORDER BY m.sentAt ASC")
    List<ChatMessage> findConversation(Integer userId1, Integer userId2);

    List<ChatMessage> findByReceiverUserIdAndIsReadFalse(Integer receiverId);

    long countByReceiverUserIdAndIsReadFalse(Integer receiverId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.sender.userId = :senderId AND m.receiver.userId = :receiverId AND m.isRead = false")
    void markConversationAsRead(Integer senderId, Integer receiverId);
}