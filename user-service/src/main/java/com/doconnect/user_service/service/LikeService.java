package com.doconnect.user_service.service;


import com.doconnect.user_service.entity.*;
import com.doconnect.user_service.exception.*;
import com.doconnect.user_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // Toggle like (like if not liked, unlike if already liked)
    public String toggleLike(Integer answerId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Answer not found"));

        // Check if already liked
        if (likeRepository.existsByUserUserIdAndAnswerAnswerId(
                user.getUserId(), answerId)) {
            // Unlike
            Like like = likeRepository
                    .findByUserUserIdAndAnswerAnswerId(
                        user.getUserId(), answerId)
                    .get();
            likeRepository.delete(like);
            return "Like removed";
        } else {
            // Like
            Like like = Like.builder()
                    .user(user)
                    .answer(answer)
                    .build();
            likeRepository.save(like);
            return "Answer liked";
        }
    }

    // Get like count for answer
    public Long getLikeCount(Integer answerId) {
        return likeRepository.countByAnswerAnswerId(answerId);
    }

    // Check if user liked an answer
    public Boolean hasUserLiked(Integer answerId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));
        return likeRepository.existsByUserUserIdAndAnswerAnswerId(
                user.getUserId(), answerId);
    }
}