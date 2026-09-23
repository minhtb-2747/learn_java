package com.employee_management.employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee_management.employee_management.Entity.Role;
import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.repository.UserRepository;
import com.employee_management.employee_management.security.JwtUtil;

@Service
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
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

  public String login(String username, String password) {
    log.info("Login: username={}", username);

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

    log.info("Login success: username={}", username);

    return jwtUtil.generateToken(userDetails.getUsername(), role);
  }
}
