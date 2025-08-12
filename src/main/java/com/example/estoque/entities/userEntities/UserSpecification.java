package com.example.estoque.entities.userEntities;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    //Filter for non-deleted users
    public static Specification<User> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    //Filter for user email
    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) ->
                email == null ? null : cb.equal(root.get("email"), email);
    }

    //Filter for user role
    public static Specification<User> hasRole(UserRole role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }
}
