package com.example.estoque.services.UserServices;

import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.authDtos.UserResponseDto;
import com.example.estoque.entities.userEntities.User;
import com.example.estoque.entities.userEntities.UserRole;
import com.example.estoque.entities.userEntities.UserSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.UserMapper;
import com.example.estoque.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private static final Set<String> SORT_WHITELIST = Set.of("id", "email", "role", "createdAt", "updatedAt");

    //GET user logic
    public PageResponseDto<UserResponseDto> getUsersSlim(String email, String role, Pageable pageable) {
        UserRole roleEnum = null;
        if (role != null && !role.isBlank()) {
            try {
                roleEnum = UserRole.valueOf(role.toUpperCase());
            }   catch (IllegalArgumentException ex) {
                throw new AppException("Invalid role", HttpStatus.BAD_REQUEST);
            }
        }

        Specification<User> spec = Specification.where
                        (UserSpecification.hasEmail(email))
                .and(UserSpecification.hasRole(roleEnum))
                .and(UserSpecification.isNotDeleted());


        Page<User> page = userRepository.findAll(spec, sanitize(pageable));

        List<UserResponseDto> content = page.map(userMapper::toDto).getContent();

        if (page.isEmpty()) {
            throw new AppException("No users found matching the given filters.", HttpStatus.NOT_FOUND);
        }

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    private Pageable sanitize(Pageable pageable) {
        if (pageable == null || pageable.getSort().isUnsorted()) return pageable;

        List<Sort.Order> safe = new ArrayList<>();
        for (Sort.Order o : pageable.getSort()) {
            if (SORT_WHITELIST.contains(o.getProperty())) {
                safe.add(o);
            } else {
                throw new AppException("Sorted fields are not allowed.", HttpStatus.BAD_REQUEST);
            }
        }
        Sort sort = safe.isEmpty() ? Sort.by("createdAt").descending() : Sort.by(safe);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
