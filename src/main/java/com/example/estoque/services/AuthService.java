package com.example.estoque.services;

import com.example.estoque.dtos.authDtos.AuthDto;
import com.example.estoque.dtos.authDtos.LoginResponseDto;
import com.example.estoque.dtos.authDtos.RegisterDto;
import com.example.estoque.entities.userEntities.User;
import com.example.estoque.entities.userEntities.UserRole;
import com.example.estoque.infra.security.TokenService;
import com.example.estoque.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public LoginResponseDto login(AuthDto dto, AuthenticationManager authenticationManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDto(token);
    }

    public ResponseEntity<?> register(RegisterDto dto) {
        if (this.userRepository.findByEmail(dto.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User newUser = new User(dto.email(), encryptedPassword);
        newUser.setRole(UserRole.USER);
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
