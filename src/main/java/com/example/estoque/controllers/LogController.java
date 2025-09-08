package com.example.estoque.controllers;

import com.example.estoque.config.Pageable.AllowedSort;
import com.example.estoque.dtos.AuditLogResponseDto;
import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.repositories.AuditLogRepository;
import com.example.estoque.services.AuditLogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Tag(name = "Log")
@SecurityRequirement(name = "BearerAuth")
public class LogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<PageResponseDto<AuditLogResponseDto>> getLogs(
            @RequestParam(required = false) String entity,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String changedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to,
            @AllowedSort(value = {"codlog", "createdAt", "entity", "action", "changedBy"},
                    defaultProp = "createdAt", defaultDir = "DESC")
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(auditLogService.getLogs(entity, action, changedBy, from, to, pageable));
    }
}
