package com.example.estoque.entities.customerEntities;

import com.example.estoque.entities.expenseEntities.Expense;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {

    //Filter for customer name
    public static Specification<Customer> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.equal(root.get("cusname"), name);
    }

    //Filter for customer address
    public static Specification<Customer> hasAddress(String address) {
        return (root, query, cb) ->
                address == null ? null : cb.equal(root.get("cusaddr"), address);
    }

    //Filter for customer city
    public static Specification<Customer> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? null : cb.equal(root.get("cuscity"), city);
    }

    //Filter for customer state
    public static Specification<Customer> hasState(String state) {
        return (root, query, cb) ->
                state == null ? null : cb.equal(root.get("cusstate"), state);
    }

    //Filter for customer zipCode
    public static Specification<Customer> hasZipCode(String zipCode) {
        return (root, query, cb) ->
                zipCode == null ? null : cb.equal(root.get("cuszip"), zipCode);
    }

    //Filter for customer phone
    public static Specification<Customer> hasPhone(String phone) {
        return (root, query, cb) ->
                phone == null ? null : cb.equal(root.get("cusphone"), phone);
    }

    //Filter for customer email
    public static Specification<Customer> hasEmail(String email) {
        return (root, query, cb) ->
                email == null ? null : cb.equal(root.get("cusemail"), email);
    }

    //Filter for expense not deleted
    public static Specification<Customer> isNotDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("isDeleted"));
    }


}
