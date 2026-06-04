package com.doconnect.admin_service.repository;

import com.doconnect.admin_service.entity.AnswerApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerApprovalRepository
        extends JpaRepository<AnswerApproval, Integer> {

    Optional<AnswerApproval> findByAnswerId(Integer answerId);
    List<AnswerApproval> findByApprovalStatus(String status);
}