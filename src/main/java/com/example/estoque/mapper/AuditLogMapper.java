package com.example.estoque.mapper;

import com.example.estoque.dtos.AuditLogResponseDto;
import com.example.estoque.entities.LogEntities.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {
    public AuditLogResponseDto toDto(AuditLog auditLog) {
        AuditLogResponseDto dto = new AuditLogResponseDto();
        dto.setCodlog(auditLog.getCodlog());
        dto.setEntity(auditLog.getEntity());
        dto.setEntityId(auditLog.getEntityId());
        dto.setAction(auditLog.getAction());
        dto.setField(auditLog.getField());
        dto.setOldValue(auditLog.getOldValue());
        dto.setNewValue(auditLog.getNewValue());
        dto.setChangedBy(auditLog.getChangedBy());
        dto.setCreatedAt(auditLog.getCreatedAt());
        return dto;
    }


}
