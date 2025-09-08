package com.example.estoque.controllers;

import com.example.estoque.config.Pageable.AllowedSort;
import com.example.estoque.dtos.authDtos.*;
import com.example.estoque.services.UserServices.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
@SecurityRequirement(name = "BearerAuth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthDto dto) {
        return ResponseEntity.ok(authService.login(dto, authenticationManager));
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto){
        return authService.register(dto);
    }

    // GET users endpoint
    @GetMapping("/see")
    public ResponseEntity<PageResponseDto<UserResponseDto>> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @AllowedSort(value = {"email", "role", "createdAt", "updatedAt"},
                         defaultProp = "createdAt", defaultDir = "DESC")
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(authService.getUsersSlim(email, role, pageable));
    }

    // Put user endpoint
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        UserResponseDto updated = authService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete user endpoint
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long id) {
        UserResponseDto deleted = authService.deleteUser(id);
        return ResponseEntity.ok(deleted);
    }
}
