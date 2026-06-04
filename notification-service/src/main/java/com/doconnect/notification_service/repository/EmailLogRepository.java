package com.doconnect.notification_service.repository;

import com.doconnect.notification_service.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailLogRepository
        extends JpaRepository<EmailLog, Integer> {

    List<EmailLog> findByStatus(String status);
    List<EmailLog> findByNotificationType(String type);
    List<EmailLog> findByRecipientEmail(String email);
}