package com.doconnect.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doconnect.user_service.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Get all comments for an answer
    List<Comment> findByAnswerAnswerId(Integer answerId);

    // Get all comments by a user
    List<Comment> findByUserUserId(Integer userId);
}