package com.example.estoque.entities.LogEntities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TGVLOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codlog;

    private String entity;

    private Long entityId;

    private String action;

    private String field;

    private String oldValue;

    private String newValue;

    private String changedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
