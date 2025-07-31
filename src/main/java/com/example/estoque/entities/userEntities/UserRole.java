package com.example.estoque.entities.userEntities;

public enum UserRole {
    ADMIN("admin"),
    MANAGER("manager"),
    OPERATOR("operator"),
    AUDITOR("auditor"),
    VIEWER("viewer");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
