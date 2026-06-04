package com.doconnect.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doconnect.user_service.entity.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // Get all approved and open questions
    List<Question> findByIsApprovedTrueAndStatus(String status);

    // Get all approved questions (any status)
    List<Question> findByIsApprovedTrue();

    // Search by keyword in title or description
    @Query("SELECT q FROM Question q WHERE q.isApproved = true " +
           "AND (LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(q.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(q.topic) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Question> searchQuestions(String keyword);

    // Get questions by user
    List<Question> findByUserUserId(Integer userId);

    // Get all unapproved questions (for admin) - excludes rejected
    @Query("SELECT q FROM Question q WHERE q.status = 'PENDING'")
    List<Question> findPendingQuestions();
}