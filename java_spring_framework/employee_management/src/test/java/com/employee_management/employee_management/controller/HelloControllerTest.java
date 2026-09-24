package com.employee_management.employee_management.controller;

import static com.employee_management.employee_management.support.TestUsers.AS_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.security.JwtUtil;

@WebMvcTest(HelloController.class)
@Import(SecurityConfig.class)
class HelloControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Test
  void hello() throws Exception {
    mockMvc.perform(get("/api/hello").with(AS_USER))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello world !"));
  }

  @Test
  void hello_requiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/hello"))
        .andExpect(status().isForbidden());
  }
}
