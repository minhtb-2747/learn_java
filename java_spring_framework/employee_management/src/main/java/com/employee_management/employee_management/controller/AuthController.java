package com.employee_management.employee_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.dto.RegisterRequest;
import com.employee_management.employee_management.dto.UserResponse;
import com.employee_management.employee_management.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    User created = authService.register(request.username(), request.password());

    return ResponseEntity.ok(new UserResponse(created.getId(), created.getUsername(), created.getRole()));
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@Valid @RequestBody RegisterRequest request) {
    System.out.println("ghfdghsfgdhsfgghdf");
    // User created = authService.register(request.username(), request.password());

    return ResponseEntity.ok("Login thanh cong");
  }

}
