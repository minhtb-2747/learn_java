package com.employee_management.employee_management.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.employee_management.employee_management.Entity.Role;
import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.AuthService;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Test
  void register_isPublicAndReturnsCreatedUser() throws Exception {
    when(authService.register("minh", "secret123")).thenReturn(new User("minh", "hashed", Role.USER));

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"username":"minh","password":"secret123"}
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("minh"))
        .andExpect(jsonPath("$.role").value("USER"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void register_returns400WhenUsernameTaken() throws Exception {
    when(authService.register(anyString(), anyString()))
        .thenThrow(new IllegalArgumentException("Username already exists: minh"));

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"username":"minh","password":"secret123"}
            """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Username already exists: minh"));
  }

  @Test
  void register_returns400WhenPasswordTooShort() throws Exception {
    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"username":"minh","password":"123"}
            """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation failed"));
  }

  @Test
  void login_returnsJwtToken() throws Exception {
    when(authService.login("admin", "admin123")).thenReturn("signed-token");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"username":"admin","password":"admin123"}
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("signed-token"));
  }

  @Test
  void login_returns401WhenCredentialsAreWrong() throws Exception {
    when(authService.login(anyString(), anyString()))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"username":"admin","password":"wrong"}
            """))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Invalid username or password"));
  }
}
