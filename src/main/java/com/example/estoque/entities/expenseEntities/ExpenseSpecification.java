package com.example.estoque.entities.expenseEntities;

import org.springframework.data.jpa.domain.Specification;

public class ExpenseSpecification {

    //Filter for expense type
    public static Specification<Expense> hasType(String type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("exptype"), type);
    }

    //Filter for expense month
    public static Specification<Expense> hasMonth(Integer month) {
        return (root, query, cb) ->
                month == null ? null : cb.equal(cb.function("DATE_PART", Integer.class, cb.literal("month"), root.get("expdate")), month);
    }

    //Filter for expense year
    public static Specification<Expense> hasYear(Integer year) {
        return (root, query, cb) ->
                year == null ? null : cb.equal(cb.function("DATE_PART", Integer.class, cb.literal("year"), root.get("expdate")), year);
    }

    //Filter for expense not deleted
    public static Specification<Expense> isNotDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("isDeleted"));
    }

    //Filter for expense payment month
    public static Specification<Expense> hasPayMonth(Integer payMonth) {
        return (root, query, cb) ->
                payMonth == null ? null : cb.equal(cb.function("DATE_PART", Integer.class, cb.literal("month"), root.get("expdatepay")), payMonth);
    }
}
