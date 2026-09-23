package com.employee_management.employee_management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.exception.EmployeeNotFoundException;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfig.class)
class EmployeeControllerTest {

  // Dùng post-processor user() thay cho @WithMockUser: filter chain đang STATELESS nên
  // SecurityContext set sẵn trước request sẽ bị NullSecurityContextRepository ghi đè
  private static final RequestPostProcessor AS_USER = user("user").roles("USER");
  private static final RequestPostProcessor AS_ADMIN = user("admin").roles("ADMIN");

  private static final String VALID_BODY = """
      {"name":"Nguyen Van A","email":"a@example.com","departmentId":1}
      """;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EmployeeService employeeService;

  // JwtAuthenticationFilter là bean Filter nên được nạp trong slice test, và nó cần JwtUtil
  @MockitoBean
  private JwtUtil jwtUtil;

  @Test
  void findAll_allowedForUserRole() throws Exception {
    when(employeeService.findAll())
        .thenReturn(List.of(new Employee("A", "a@example.com", new Department("IT"))));

    mockMvc.perform(get("/api/employees").with(AS_USER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("A"))
        .andExpect(jsonPath("$[0].department.name").value("IT"));
  }

  @Test
  void findById_returns404WhenMissing() throws Exception {
    when(employeeService.findById(99L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/employees/99").with(AS_ADMIN))
        .andExpect(status().isNotFound());
  }

  @Test
  void findAll_rejectedWithEmptyBodyWhenNotAuthenticated() throws Exception {
    // 403 (không phải 401) và không có body: bị chặn ở tầng filter bởi Http403ForbiddenEntryPoint,
    // chưa vào tới DispatcherServlet nên GlobalExceptionHandler không can thiệp được
    mockMvc.perform(get("/api/employees"))
        .andExpect(status().isForbidden())
        .andExpect(content().string(""));
  }

  @Test
  void createEmployee_allowedForAdminRole() throws Exception {
    when(employeeService.createEmployee(anyString(), anyString(), anyLong()))
        .thenReturn(new Employee("NGUYEN VAN A", "a@example.com", new Department("IT")));

    mockMvc.perform(post("/api/employees").with(AS_ADMIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("NGUYEN VAN A"));
  }

  @Test
  void createEmployee_forbiddenForUserRole() throws Exception {
    // đã đăng nhập nhưng sai role: @PreAuthorize ném AccessDeniedException bên trong
    // DispatcherServlet nên GlobalExceptionHandler bắt được và trả body JSON
    mockMvc.perform(post("/api/employees").with(AS_USER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(VALID_BODY))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.message").value("You do not have permission to perform this action"));

    verify(employeeService, never()).createEmployee(any(), any(), any());
  }

  @Test
  void updateEmployee_forbiddenForUserRole() throws Exception {
    mockMvc.perform(put("/api/employees/1").with(AS_USER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(VALID_BODY))
        .andExpect(status().isForbidden());
  }

  @Test
  void deleteEmployee_forbiddenForUserRole() throws Exception {
    mockMvc.perform(delete("/api/employees/1").with(AS_USER))
        .andExpect(status().isForbidden());

    verify(employeeService, never()).deleteEmployee(anyLong());
  }

  @Test
  void deleteEmployee_allowedForAdminRole() throws Exception {
    mockMvc.perform(delete("/api/employees/1").with(AS_ADMIN))
        .andExpect(status().isNoContent());

    verify(employeeService).deleteEmployee(1L);
  }

  @Test
  void createEmployee_returns400WhenValidationFails() throws Exception {
    String invalidBody = """
        {"name":"","email":"not-an-email","departmentId":null}
        """;

    mockMvc.perform(post("/api/employees").with(AS_ADMIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors").isArray());
  }

  @Test
  void deleteEmployee_returns404WhenServiceThrows() throws Exception {
    doThrow(new EmployeeNotFoundException(99L)).when(employeeService).deleteEmployee(99L);

    mockMvc.perform(delete("/api/employees/99").with(AS_ADMIN))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }
}
