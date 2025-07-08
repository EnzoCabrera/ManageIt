package com.example.estoque.services;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(String entity, Long entityId, String action, String field, String oldValue, String newValue, String changedBy) {
        AuditLog log = AuditLog.builder()
                .entity(entity)
                .entityId(entityId)
                .action(action)
                .field(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedBy(changedBy)
                .build();

        auditLogRepository.save(log);
    }
}
