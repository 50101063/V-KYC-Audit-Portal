package com.vkyc.auditportal.repository;

import com.vkyc.auditportal.model.RecordMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This is a placeholder. In a real application, you would define methods
// to query for RecordMetadata based on various criteria.
// For now, we use a simple in-memory simulation in the controller.
@Repository
public interface RecordRepository extends JpaRepository<RecordMetadata, Long> {
    // Example: List<RecordMetadata> findByUserId(String userId);
    // This interface will be expanded by the Database Developer.
}
