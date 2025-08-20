package com.example.estoque.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditLogResponseDto {

    private Long codlog;

    private String entity;

    private Long entityId;

    private String action;

    private String field;

    private String oldValue;

    private String newValue;

    private String changedBy;

    private LocalDateTime createdAt;
}
