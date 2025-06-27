package com.example.estoque.controllers;

import com.example.estoque.dtos.AuthDto;
import com.example.estoque.dtos.LoginResponseDto;
import com.example.estoque.dtos.RegisterDto;
import com.example.estoque.entities.User;
import com.example.estoque.entities.UserRole;
import com.example.estoque.infra.security.TokenService;
import com.example.estoque.repositories.UserRepository;
import com.example.estoque.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
