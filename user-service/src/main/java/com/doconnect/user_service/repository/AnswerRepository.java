package com.doconnect.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doconnect.user_service.entity.Answer;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    // Get approved answers for a question
    List<Answer> findByQuestionQuestionIdAndIsApprovedTrue(Integer questionId);

    // Get all answers by a user
    List<Answer> findByUserUserId(Integer userId);

    // Get unapproved answers (for admin)
    List<Answer> findByIsApprovedFalse();

    // Get all answers for a question (for admin)
    List<Answer> findByQuestionQuestionId(Integer questionId);

    // Get pending answer by user for a question
    Answer findByQuestionQuestionIdAndUserUserIdAndIsApprovedFalse(Integer questionId, Integer userId);
}