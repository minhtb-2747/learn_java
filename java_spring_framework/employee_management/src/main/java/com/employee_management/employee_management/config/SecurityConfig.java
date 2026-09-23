package com.employee_management.employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.employee_management.employee_management.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity // bật phân quyền ở cấp method
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // Không dùng CSRF cho API JWT
        .csrf(csrf -> csrf.disable())
        // Không dùng Session để lưu trạng thái login
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Quyền truy cập API
        .authorizeHttpRequests(auth -> auth
            // Login/Register không cần JWT
            .requestMatchers("/api/auth/**").permitAll()
            // Trang Thymeleaf (server-side render, không gọi qua /api) - public,
            .requestMatchers("/employees/**", "/css/**").permitAll()
            // Các API khác phải có JWT hợp lệ
            .anyRequest().authenticated())
        // Cho JWT Filter xử lý JWT trước
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
