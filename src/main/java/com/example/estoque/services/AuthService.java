package com.example.estoque.services;

import com.example.estoque.dtos.authDtos.AuthDto;
import com.example.estoque.dtos.authDtos.LoginResponseDto;
import com.example.estoque.dtos.authDtos.RegisterDto;
import com.example.estoque.dtos.authDtos.UserResponseDto;
import com.example.estoque.entities.userEntities.User;
import com.example.estoque.entities.userEntities.UserRole;
import com.example.estoque.entities.userEntities.UserSpecification;
import com.example.estoque.infra.security.TokenService;
import com.example.estoque.mapper.UserMapper;
import com.example.estoque.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    //Login logic
    public LoginResponseDto login(AuthDto dto, AuthenticationManager authenticationManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken((User) auth.getPrincipal());

        //Log the login event
        auditLogService.log(
                "User",
                user.getId(),
                "LOGIN",
                "last_login",
                null,
                "SUCCESS",
                user.getEmail()
        );

        return new LoginResponseDto(token);
    }

    //Register logic
    public ResponseEntity<?> register(RegisterDto dto) {
        if (this.userRepository.findByEmail(dto.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User newUser = new User(dto.email(), encryptedPassword, dto.role() != null ? dto.role() : UserRole.VIEWER);
        this.userRepository.save(newUser);

        auditLogService.log(
                "User",
                newUser.getId(),
                "REGISTER",
                "account_status",
                null,
                "CREATED",
                newUser.getEmail()
        );

        return ResponseEntity.ok().build();
    }

    //GET user logic
    public List<UserResponseDto> getFilterUsers(String email, String role) {
        UserRole roleEnum = null;
        if (role != null && !role.isBlank()) {
            try {
                roleEnum = UserRole.valueOf(role.toUpperCase());
            }   catch (IllegalArgumentException ex) {
                return List.of();
            }
        }

        Specification<User> spec = Specification.where
                    (UserSpecification.hasEmail(email))
                .and(UserSpecification.hasRole(roleEnum));

        return userRepository.findAll(spec)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}
