package com.employee_management.employee_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.employee_management.employee_management.Entity.Role;
import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.repository.UserRepository;
import com.employee_management.employee_management.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private AuthService authService;

  @Test
  void register_encodesPasswordAndDefaultsToUserRole() {
    when(userRepository.existsByUsername("minh")).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("hashed");
    when(userRepository.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

    authService.register("minh", "secret123");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());

    User saved = captor.getValue();
    assertThat(saved.getUsername()).isEqualTo("minh");
    assertThat(saved.getPassword()).isEqualTo("hashed");
    assertThat(saved.getRole()).isEqualTo(Role.USER);
  }

  @Test
  void register_throwsWhenUsernameAlreadyExists() {
    when(userRepository.existsByUsername("minh")).thenReturn(true);

    assertThatThrownBy(() -> authService.register("minh", "secret123"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("minh");

    verify(userRepository, never()).save(any());
  }

  @Test
  void login_returnsTokenGeneratedFromAuthenticatedUser() {
    UserDetails principal = new org.springframework.security.core.userdetails.User(
        "admin", "hashed", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        principal, null, principal.getAuthorities());

    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    when(jwtUtil.generateToken("admin", "ADMIN")).thenReturn("signed-token");

    assertThat(authService.login("admin", "admin123")).isEqualTo("signed-token");
  }

  @Test
  void login_propagatesAuthenticationFailure() {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    assertThatThrownBy(() -> authService.login("admin", "wrong"))
        .isInstanceOf(BadCredentialsException.class);

    verify(jwtUtil, never()).generateToken(any(), any());
  }
}
