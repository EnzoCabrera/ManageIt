package com.example.estoque.services;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getLogsByEntity(String entity) {
        return auditLogRepository.findByEntity(entity.toUpperCase());
    }

    public void log(String entity, Long entityId, String action, String field, String oldValue, String newValue, String changedBy) {
        AuditLog log = new AuditLog();
        log.setEntity(entity.toUpperCase());
        log.setEntityId(entityId);
        log.setAction(action);
        log.setField(field);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setChangedBy(changedBy);
        auditLogRepository.save(log);
    }
}
