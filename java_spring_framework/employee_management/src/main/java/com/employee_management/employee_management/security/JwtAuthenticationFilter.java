package com.employee_management.employee_management.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String HEADER_PREFIX = "Bearer ";

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Lấy Authorization header
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith(HEADER_PREFIX)) {
      String token = authHeader.substring(HEADER_PREFIX.length()); // Bỏ "Bearer "

      if (jwtUtil.isTokenValid(token)) {
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        // Tạo Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

        // Lưu vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response); // Không có Bearer token => cho request đi tiếp
  }
}
