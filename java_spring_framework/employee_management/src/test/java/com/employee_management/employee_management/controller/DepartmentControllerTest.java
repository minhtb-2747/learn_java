package com.employee_management.employee_management.controller;

import static com.employee_management.employee_management.support.TestUsers.AS_ADMIN;
import static com.employee_management.employee_management.support.TestUsers.AS_USER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.DepartmentService;

@WebMvcTest(DepartmentController.class)
@Import(SecurityConfig.class)
class DepartmentControllerTest {

  private static final String SALES_BODY = """
      {"name":"Sales"}
      """;

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
  void create_succeedsForAdmin() throws Exception {
    when(departmentService.create(anyString())).thenReturn(new Department("Sales"));

    mockMvc.perform(post("/api/departments").with(AS_ADMIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(SALES_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Sales"));
  }

  @Test
  void create_isForbiddenForPlainUser() throws Exception {
    mockMvc.perform(post("/api/departments").with(AS_USER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(SALES_BODY))
        .andExpect(status().isForbidden());

    verify(departmentService, never()).create(anyString());
  }

  @Test
  void update_isForbiddenForPlainUser() throws Exception {
    mockMvc.perform(put("/api/departments/1").with(AS_USER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(SALES_BODY))
        .andExpect(status().isForbidden());

    verify(departmentService, never()).update(anyLong(), anyString());
  }

  @Test
  void delete_isForbiddenForPlainUser() throws Exception {
    mockMvc.perform(delete("/api/departments/1").with(AS_USER))
        .andExpect(status().isForbidden());

    verify(departmentService, never()).delete(anyLong());
  }

  @Test
  void delete_returns400WhenDepartmentMissing() throws Exception {
    doThrow(new IllegalArgumentException("Department not found with id: 99"))
        .when(departmentService).delete(99L);

    mockMvc.perform(delete("/api/departments/99").with(AS_ADMIN))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Department not found with id: 99"));
  }
}
