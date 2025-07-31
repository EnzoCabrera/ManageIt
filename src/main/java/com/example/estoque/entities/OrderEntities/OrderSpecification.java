package com.example.estoque.entities.OrderEntities;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderSpecification {

    //Filter for order code
    public static Specification<Order> hasCodord(Long codord) {
        return (root, query, cb) ->
                codord == null ? null : cb.equal(root.get("codord"), codord);
    }

    //Filter for customer code
    public static Specification<Order> hasCustomerCode(Long codcus) {
        return (root, query, cb) ->
                codcus == null ? null  : cb.equal(root.get("codcus").get("codcus"), codcus);
    }

    //Filter by non deleted orders
    public static Specification<Order> isNotDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("isDeleted"));
    }

    //Filter by order date range
    public static Specification<Order> isBetweenCreatedAt(LocalDate startDate, LocalDate endDate ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (startDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            return predicate;
        };
    }

    //Filter by order status
    public static Specification<Order> hasOrderSts(String ordsts) {
        return (root, query, cb) ->
                ordsts == null ? null : cb.equal(root.get("ordsts"), ordsts);
    }

    public static Specification<Order> hasOrderPayType(String ordpaytype) {
        return (root, query, cb) ->
                ordpaytype == null ? null : cb.equal(root.get("ordpaytype"), ordpaytype);
    }
}
