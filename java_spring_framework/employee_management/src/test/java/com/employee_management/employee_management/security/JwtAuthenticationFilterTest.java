package com.employee_management.employee_management.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private final MockHttpServletRequest request = new MockHttpServletRequest();
  private final MockHttpServletResponse response = new MockHttpServletResponse();

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void validToken_populatesSecurityContext() throws Exception {
    request.addHeader("Authorization", "Bearer valid-token");
    when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);
    when(jwtUtil.extractUsername("valid-token")).thenReturn("admin");
    when(jwtUtil.extractRole("valid-token")).thenReturn("ADMIN");

    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getPrincipal()).isEqualTo("admin");
    assertThat(authentication.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_ADMIN");
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void invalidToken_leavesSecurityContextEmpty() throws Exception {
    request.addHeader("Authorization", "Bearer broken-token");
    when(jwtUtil.isTokenValid("broken-token")).thenReturn(false);

    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void missingHeader_skipsAuthenticationButContinuesChain() throws Exception {
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void nonBearerHeader_isIgnored() throws Exception {
    request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }
}
