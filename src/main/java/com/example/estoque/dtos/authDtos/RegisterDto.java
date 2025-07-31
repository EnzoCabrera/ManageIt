package com.example.estoque.dtos.authDtos;

import com.example.estoque.entities.userEntities.UserRole;

public record RegisterDto(String email, String password, UserRole role) {
}
