package com.example.estoque.controllers;

import com.example.estoque.dtos.authDtos.AuthDto;
import com.example.estoque.dtos.authDtos.LoginResponseDto;
import com.example.estoque.dtos.authDtos.RegisterDto;
import com.example.estoque.dtos.authDtos.UserResponseDto;
import com.example.estoque.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<List<UserResponseDto>> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        List<UserResponseDto> users = authService.getFilterUsers(email, role);
        return ResponseEntity.ok(users);
    }
}
