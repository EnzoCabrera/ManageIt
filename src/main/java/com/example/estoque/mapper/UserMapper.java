package com.example.estoque.mapper;

import com.example.estoque.dtos.authDtos.UserResponseDto;
import com.example.estoque.entities.userEntities.User;
import com.example.estoque.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;

    public UserResponseDto toDto(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        dto.setIsDeleted(user.getIsDeleted());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}
