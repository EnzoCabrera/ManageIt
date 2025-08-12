package com.example.estoque.services;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getLogsByEntity(String entity) {

        if (entity == null || entity.isEmpty()) {
            throw new AppException("No users found matching the given filters.", HttpStatus.NOT_FOUND);
        }
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
