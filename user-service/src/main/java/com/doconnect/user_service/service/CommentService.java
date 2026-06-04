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
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    // Add comment
    public CommentDTO addComment(Integer answerId,
            CommentRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Answer not found"));

        Comment comment = Comment.builder()
                .commentText(request.getCommentText())
                .user(user)
                .answer(answer)
                .build();

        commentRepository.save(comment);
        return mapToDTO(comment);
    }

    // Get comments for an answer
    public List<CommentDTO> getCommentsForAnswer(Integer answerId) {
        return commentRepository.findByAnswerAnswerId(answerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Delete comment
    public String deleteComment(Integer commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getUsername().equals(username))
            throw new ResourceNotFoundException("Not authorized");

        commentRepository.delete(comment);
        return "Comment deleted";
    }

    private CommentDTO mapToDTO(Comment c) {
        return CommentDTO.builder()
                .commentId(c.getCommentId())
                .commentText(c.getCommentText())
                .commentedBy(c.getUser().getUsername())
                .userId(c.getUser().getUserId())
                .answerId(c.getAnswer().getAnswerId())
                .createdAt(c.getCreatedAt())
                .build();
    }
}