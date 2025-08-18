package com.example.estoque.services;

import com.example.estoque.dtos.AuditLogResponseDto;
import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.entities.LogEntities.AuditLogSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.AuditLogMapper;
import com.example.estoque.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogMapper auditLogMapper;

    //GET logs logic
    public PageResponseDto<AuditLogResponseDto> getLogs(
            String entity,
            String action,
            String changedBy,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable) {

        Specification<AuditLog> spec = Specification
                .where(AuditLogSpecification.hasEntity(entity))
                .and(AuditLogSpecification.hasAction(action))
                .and(AuditLogSpecification.hasChangedBy(changedBy))
                .and(AuditLogSpecification.createdBetween(from, to));

        Page<AuditLog> page = auditLogRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            throw new AppException("No logs found for the given filters", HttpStatus.NOT_FOUND);
        }

        List<AuditLogResponseDto> content = page.getContent()
                .stream()
                .map(auditLogMapper::toDto)
                .toList();

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    //Log registration logic
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
