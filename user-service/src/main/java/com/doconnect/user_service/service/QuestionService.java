package com.doconnect.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.doconnect.user_service.dto.QuestionDTO;
import com.doconnect.user_service.dto.QuestionRequest;
import com.doconnect.user_service.entity.Question;
import com.doconnect.user_service.entity.User;
import com.doconnect.user_service.exception.ResourceNotFoundException;
import com.doconnect.user_service.repository.QuestionRepository;
import com.doconnect.user_service.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WebClient webClient;

    // Ask a question
    public QuestionDTO askQuestion(QuestionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        Question question = Question.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .topic(request.getTopic())
                .status("PENDING")
                .isApproved(false)
                .user(user)
                .build();

        questionRepository.save(question);
        // 🔔 Notify admin about new question
        try {
            Map<String, Object> notificationRequest = new HashMap<>();
            notificationRequest.put("questionId", question.getQuestionId());
            notificationRequest.put("questionTitle", question.getTitle());
            notificationRequest.put("askedBy", user.getUsername());
            notificationRequest.put("topic", question.getTopic());

            webClient.post()
                    .uri("/api/notifications/question-asked")
                    .bodyValue(notificationRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(); // async - dont wait
        } catch (Exception e) {
            // dont fail if notification fails
            System.out.println("Notification failed: " + e.getMessage());
        }
        return mapToDTO(question);
    }

    // Get all approved questions
    public List<QuestionDTO> getAllApprovedQuestions() {
        return questionRepository
                .findByIsApprovedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get all questions (for admin)
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Search questions
    public List<QuestionDTO> searchQuestions(String keyword) {
        return questionRepository.searchQuestions(keyword)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get question by ID
    public QuestionDTO getQuestionById(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Question not found with id: " + questionId));
        return mapToDTO(question);
    }

    // Get questions by user
    public List<QuestionDTO> getQuestionsByUser(Integer userId) {
        return questionRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get my questions (authenticated user)
    public List<QuestionDTO> getMyQuestions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return questionRepository.findByUserUserId(user.getUserId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update question
    public QuestionDTO updateQuestion(Integer questionId,
            QuestionRequest request, String username) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));

        if (!question.getUser().getUsername().equals(username))
            throw new ResourceNotFoundException("Not authorized");

        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());
        question.setTopic(request.getTopic());
        question.setIsApproved(false);
        question.setStatus("PENDING"); // back to pending after edit

        questionRepository.save(question);
        return mapToDTO(question);
    }

    // Delete question
    public String deleteQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));
        questionRepository.delete(question);
        return "Question deleted successfully";
    }

    // Approve question (called from admin service)
    public String approveQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));
        question.setIsApproved(true);
        question.setStatus("OPEN");
        questionRepository.save(question);
        return "Question approved";
    }

    // Reject question (called from admin service)
    public String rejectQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));
        question.setIsApproved(false);
        question.setStatus("REJECTED");
        questionRepository.save(question);
        return "Question rejected";
    }

    // Close/resolve question (admin)
    public String closeQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question not found"));
        question.setStatus("RESOLVED");
        questionRepository.save(question);
        return "Question marked as resolved";
    }

    // Get unapproved (pending) questions - excludes rejected
    public List<QuestionDTO> getUnapprovedQuestions() {
        return questionRepository.findPendingQuestions()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private QuestionDTO mapToDTO(Question q) {
        return QuestionDTO.builder()
                .questionId(q.getQuestionId())
                .title(q.getTitle())
                .description(q.getDescription())
                .topic(q.getTopic())
                .status(q.getStatus())
                .isApproved(q.getIsApproved())
                .askedBy(q.getUser().getUsername())
                .userId(q.getUser().getUserId())
                .createdAt(q.getCreatedAt())
                .build();
    }
}
