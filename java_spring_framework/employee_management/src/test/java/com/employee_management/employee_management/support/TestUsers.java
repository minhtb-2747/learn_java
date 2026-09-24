package com.employee_management.employee_management.support;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

public final class TestUsers {

  public static final RequestPostProcessor AS_USER = user("user").roles("USER");

  public static final RequestPostProcessor AS_ADMIN = user("admin").roles("ADMIN");

  private TestUsers() {
  }
}
