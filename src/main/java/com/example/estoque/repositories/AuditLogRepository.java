package com.example.estoque.repositories;

import com.example.estoque.entities.LogEntities.AuditLog;
import com.example.estoque.entities.OrderEntities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

    //Query to get logs by entity
    List<AuditLog> findByEntity(String entity);
}
