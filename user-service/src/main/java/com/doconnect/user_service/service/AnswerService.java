package com.doconnect.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.doconnect.user_service.dto.AnswerDTO;
import com.doconnect.user_service.dto.AnswerRequest;
import com.doconnect.user_service.entity.Answer;
import com.doconnect.user_service.entity.Question;
import com.doconnect.user_service.entity.User;
import com.doconnect.user_service.exception.QuestionClosedException;
import com.doconnect.user_service.exception.ResourceNotFoundException;
import com.doconnect.user_service.repository.AnswerRepository;
import com.doconnect.user_service.repository.LikeRepository;
import com.doconnect.user_service.repository.QuestionRepository;
import com.doconnect.user_service.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private WebClient webClient;

    // Post an answer
    public AnswerDTO postAnswer(Integer questionId,
            AnswerRequest request, String username) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));

        // Only allow answers on OPEN questions
        if (!question.getStatus().equals("OPEN"))
            throw new QuestionClosedException(
                "This question is closed or resolved");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        Answer answer = Answer.builder()
                .answerText(request.getAnswerText())
                .isApproved(false)
                .user(user)
                .question(question)
                .build();

        answerRepository.save(answer);
        
     // 🔔 Notify admin about new answer
        try {
            Map<String, Object> notificationRequest = new HashMap<>();
            notificationRequest.put("answerId", answer.getAnswerId());
            notificationRequest.put("questionId", question.getQuestionId());
            notificationRequest.put("questionTitle", question.getTitle());
            notificationRequest.put("answeredBy", user.getUsername());
            notificationRequest.put("answerText", answer.getAnswerText());

            webClient.post()
                    .uri("/api/notifications/answer-posted")
                    .bodyValue(notificationRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(); // async
        } catch (Exception e) {
            System.out.println("Notification failed: " + e.getMessage());
        }
        
        return mapToDTO(answer);
    }

    // Get current user's pending (unapproved) answer for a question
    public AnswerDTO getMyPendingAnswer(Integer questionId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Answer answer = answerRepository
                .findByQuestionQuestionIdAndUserUserIdAndIsApprovedFalse(
                        questionId, user.getUserId());
        return answer != null ? mapToDTO(answer) : null;
    }

    // Get approved answers for a question
    public List<AnswerDTO> getApprovedAnswers(Integer questionId) {
        return answerRepository
                .findByQuestionQuestionIdAndIsApprovedTrue(questionId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get all answers for a question (admin)
    public List<AnswerDTO> getAllAnswersForQuestion(Integer questionId) {
        return answerRepository
                .findByQuestionQuestionId(questionId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get all answers (admin)
    public List<AnswerDTO> getAllAnswers() {
        return answerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get unapproved answers (admin)
    public List<AnswerDTO> getUnapprovedAnswers() {
        return answerRepository.findByIsApprovedFalse()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Approve answer
    public String approveAnswer(Integer answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Answer not found"));
        answer.setIsApproved(true);
        answerRepository.save(answer);
        return "Answer approved";
    }

    // Delete answer
    public String deleteAnswer(Integer answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Answer not found"));
        answerRepository.delete(answer);
        return "Answer deleted";
    }

    // Update answer
    public AnswerDTO updateAnswer(Integer answerId,
            AnswerRequest request, String username) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Answer not found"));

        if (!answer.getUser().getUsername().equals(username))
            throw new ResourceNotFoundException("Not authorized");

        answer.setAnswerText(request.getAnswerText());
        answer.setIsApproved(false);
        answerRepository.save(answer);
        return mapToDTO(answer);
    }

    private AnswerDTO mapToDTO(Answer a) {
        Long likeCount = likeRepository
                .countByAnswerAnswerId(a.getAnswerId());
        return AnswerDTO.builder()
                .answerId(a.getAnswerId())
                .answerText(a.getAnswerText())
                .isApproved(a.getIsApproved())
                .answeredBy(a.getUser().getUsername())
                .userId(a.getUser().getUserId())
                .questionId(a.getQuestion().getQuestionId())
                .likeCount(likeCount)
                .createdAt(a.getCreatedAt())
                .build();
    }
}