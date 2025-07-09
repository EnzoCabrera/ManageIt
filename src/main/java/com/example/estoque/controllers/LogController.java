package com.example.estoque.controllers;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.repositories.AuditLogRepository;
import com.example.estoque.services.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLog> getLogsByEntity(@RequestParam String entity) {
        return auditLogService.getLogsByEntity(entity);
    }
}
