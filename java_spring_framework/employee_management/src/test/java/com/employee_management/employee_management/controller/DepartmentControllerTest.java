package com.employee_management.employee_management.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.DepartmentService;

@WebMvcTest(DepartmentController.class)
@Import(SecurityConfig.class)
class DepartmentControllerTest {

  private static final RequestPostProcessor AS_USER = user("user").roles("USER");

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DepartmentService departmentService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Test
  void findAll_requiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/departments"))
        .andExpect(status().isForbidden());
  }

  @Test
  void findAll_returnsDepartmentsForAuthenticatedUser() throws Exception {
    when(departmentService.findAll()).thenReturn(List.of(new Department("IT")));

    mockMvc.perform(get("/api/departments").with(AS_USER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("IT"));
  }

  @Test
  void create_isAllowedForPlainUser() throws Exception {
    // DepartmentController không có @PreAuthorize nên mọi user đã đăng nhập đều ghi được,
    // khác với EmployeeController (chỉ ADMIN mới được ghi)
    when(departmentService.create(anyString())).thenReturn(new Department("Sales"));

    mockMvc.perform(post("/api/departments").with(AS_USER)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {"name":"Sales"}
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Sales"));
  }

  @Test
  void delete_returns400WhenDepartmentMissing() throws Exception {
    doThrow(new IllegalArgumentException("Department not found with id: 99"))
        .when(departmentService).delete(99L);

    mockMvc.perform(delete("/api/departments/99").with(AS_USER))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Department not found with id: 99"));
  }
}
