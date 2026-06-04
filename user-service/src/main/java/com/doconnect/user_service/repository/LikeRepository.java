package com.doconnect.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doconnect.user_service.entity.Like;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    // Check if user already liked an answer
    Optional<Like> findByUserUserIdAndAnswerAnswerId(
        Integer userId, Integer answerId);

    // Count likes for an answer
    Long countByAnswerAnswerId(Integer answerId);

    // Check if like exists
    Boolean existsByUserUserIdAndAnswerAnswerId(
        Integer userId, Integer answerId);
}