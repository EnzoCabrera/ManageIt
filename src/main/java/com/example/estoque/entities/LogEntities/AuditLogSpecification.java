package com.example.estoque.entities.LogEntities;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AuditLogSpecification {

    public static Specification<AuditLog> hasEntity(String entity) {
        return (root, q, cb) ->
                (entity == null || entity.isBlank())
                        ? cb.conjunction()
                        : cb.equal(cb.upper(root.get("entity")), entity.trim().toUpperCase());
    }

    public static Specification<AuditLog> hasAction(String action) {
        return (root, q, cb) ->
                (action == null || action.isBlank())
                        ? cb.conjunction()
                        : cb.equal(cb.upper(root.get("action")), action.trim().toUpperCase());
    }

    public static Specification<AuditLog> hasChangedBy(String changedBy) {
        return (root, q, cb) ->
                (changedBy == null || changedBy.isBlank())
                        ? cb.conjunction()
                        : cb.equal(cb.upper(root.get("changedBy")), changedBy.trim().toLowerCase());
    }

    public static Specification<AuditLog> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, q, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null) return cb.between(root.get("createdAt"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);

        };
    }
}
