package com.employee_management.employee_management.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.employee_management.employee_management.Entity.Role;
import com.employee_management.employee_management.Entity.User;
import com.employee_management.employee_management.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  @Test
  void loadUserByUsername_mapsRoleToPrefixedAuthority() {
    when(userRepository.findByUsername("admin"))
        .thenReturn(Optional.of(new User("admin", "hashed", Role.ADMIN)));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

    assertThat(userDetails.getUsername()).isEqualTo("admin");
    assertThat(userDetails.getPassword()).isEqualTo("hashed");
    assertThat(userDetails.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_ADMIN");
  }

  @Test
  void loadUserByUsername_throwsWhenUserNotFound() {
    when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("ghost"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("ghost");
  }
}
