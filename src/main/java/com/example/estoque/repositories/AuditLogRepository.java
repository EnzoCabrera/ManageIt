package com.example.estoque.repositories;

import com.example.estoque.entities.LogEntities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
