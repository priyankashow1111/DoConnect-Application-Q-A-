package com.doconnect.admin_service.repository;

import com.doconnect.admin_service.entity.QuestionApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionApprovalRepository
        extends JpaRepository<QuestionApproval, Integer> {

    Optional<QuestionApproval> findByQuestionId(Integer questionId);
    List<QuestionApproval> findByApprovalStatus(String status);
}