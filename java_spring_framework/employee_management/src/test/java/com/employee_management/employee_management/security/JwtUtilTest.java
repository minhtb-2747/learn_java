package com.employee_management.employee_management.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

  // HS256 yêu cầu key tối thiểu 256 bit (32 byte)
  private static final String SECRET = "test-secret-key-for-unit-test-1234567890";
  private static final long ONE_HOUR = 3_600_000L;

  private final JwtUtil jwtUtil = new JwtUtil(SECRET, ONE_HOUR);

  @Test
  void generateToken_thenExtractUsernameAndRole() {
    String token = jwtUtil.generateToken("minh", "ADMIN");

    assertThat(jwtUtil.extractUsername(token)).isEqualTo("minh");
    assertThat(jwtUtil.extractRole(token)).isEqualTo("ADMIN");
    assertThat(jwtUtil.isTokenValid(token)).isTrue();
  }

  @Test
  void isTokenValid_falseForMalformedToken() {
    assertThat(jwtUtil.isTokenValid("not-a-jwt")).isFalse();
  }

  @Test
  void isTokenValid_falseForExpiredToken() {
    JwtUtil expiredIssuer = new JwtUtil(SECRET, -1000L);
    String expiredToken = expiredIssuer.generateToken("minh", "USER");

    assertThat(jwtUtil.isTokenValid(expiredToken)).isFalse();
  }

  @Test
  void isTokenValid_falseWhenSignedWithDifferentSecret() {
    JwtUtil otherIssuer = new JwtUtil("another-secret-key-totally-different-123456", ONE_HOUR);
    String foreignToken = otherIssuer.generateToken("minh", "USER");

    assertThat(jwtUtil.isTokenValid(foreignToken)).isFalse();
  }
}
