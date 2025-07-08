package com.example.estoque.controllers;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.repositories.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {

    private final AuditLogRepository auditLogRepository;

    public LogController(AuditLogRepository auditLogRepository) { this.auditLogRepository = auditLogRepository; }

    @GetMapping("/see")
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogRepository.findAll());
    }
}
