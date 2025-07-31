package com.example.estoque.infra.security;

import com.example.estoque.exceptions.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        //public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                        //secured endpoints
                        .requestMatchers(HttpMethod.POST, "/api/stock/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/stock/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR", "AUDITOR", "VIEWER")
                        .requestMatchers(HttpMethod.PUT, "/api/stock/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/stock/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/order/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR")
                        .requestMatchers(HttpMethod.GET, "/api/order/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR", "AUDITOR", "VIEWER")
                        .requestMatchers(HttpMethod.PUT, "/api/order/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/order/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/expense/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/expense/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR", "AUDITOR", "VIEWER")
                        .requestMatchers(HttpMethod.PUT, "/api/expense/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/expense/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/customer/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/customer/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR", "AUDITOR", "VIEWER")
                        .requestMatchers(HttpMethod.PUT, "/api/customer/**").hasAnyRole("ADMIN", "MANAGER", "OPERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/customer/**").hasAnyRole("ADMIN")

                        .requestMatchers("/api/log/**").hasAnyRole("ADMIN", "AUDITOR")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
