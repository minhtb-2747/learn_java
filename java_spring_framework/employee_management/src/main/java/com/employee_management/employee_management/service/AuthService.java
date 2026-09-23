package com.employee_management.employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee_management.employee_management.Entity.Role;
import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.repository.UserRepository;

@Service
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(String username, String password) {
    log.info("Registering user: username={}", username);

    if (userRepository.existsByUsername(username)) {
      log.warn("Register failed: username already exists, username={}", username);
      throw new IllegalArgumentException("Username already exists: " + username);
    }

    User user = new User(username, passwordEncoder.encode(password), Role.USER);
    User saved = userRepository.save(user);

    log.info("Registered user: id={}, username={}", saved.getId(), saved.getUsername());

    return saved;
  }
}
