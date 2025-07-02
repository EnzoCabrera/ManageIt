package com.example.estoque.entities.expenseEntities;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExpenseSpecification {

    //Filter for expense type
    public static Specification<Expense> hasType(String type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("exptype"), type);
    }

    //Filter for expense not deleted
    public static Specification<Expense> isNotDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("isDeleted"));
    }

    //Filter for expense date range
    public static Specification<Expense> isBetweenExpdate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (startDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("expdate"), startDate));
            }
            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("expdate"), endDate));
            }

            return predicate;
        };
    }

    //Filter for expense payment date range
    public static Specification<Expense> isBetweenExpdatepay(LocalDate startDatePay, LocalDate endDatePay) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (startDatePay != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("expdatepay"), startDatePay));
            }
            if (endDatePay != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("expdatepay"), endDatePay));
            }

            return predicate;
        };
    }

    //Filter for expense status
    public static Specification<Expense> hasExpSts(String expSts) {
        return (root, query, cb) ->
                expSts == null ? null : cb.equal(root.get("expsts"), expSts);

    }
}
