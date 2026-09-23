package com.employee_management.employee_management.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.dto.DepartmentStatsResponse;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.DepartmentService;
import com.employee_management.employee_management.service.EmployeeService;
import com.employee_management.employee_management.service.StatisticsService;

@WebMvcTest(EmployeeViewController.class)
@Import(SecurityConfig.class)
class EmployeeViewControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EmployeeService employeeService;

  @MockitoBean
  private DepartmentService departmentService;

  @MockitoBean
  private StatisticsService statisticsService;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Test
  void list_isPublicBecauseThymeleafRoutesArePermitAll() throws Exception {
    when(employeeService.findAll())
        .thenReturn(List.of(new Employee("A", "a@example.com", new Department("IT"))));

    mockMvc.perform(get("/employees/list"))
        .andExpect(status().isOk())
        .andExpect(view().name("employees/list"))
        .andExpect(model().attributeExists("employees"));
  }

  @Test
  void list_filtersByNameWhenParamProvided() throws Exception {
    when(employeeService.searchByName("a")).thenReturn(List.of());

    mockMvc.perform(get("/employees/list").param("name", "a"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("name", "a"));

    verify(employeeService).searchByName("a");
    verify(employeeService, never()).findAll();
  }

  @Test
  void statistics_rendersTotalsAndDepartmentBreakdown() throws Exception {
    when(statisticsService.countEmployeesByDepartment()).thenReturn(List.<DepartmentStatsResponse>of());
    when(statisticsService.countTotalEmployees()).thenReturn(5L);

    mockMvc.perform(get("/employees/statistics"))
        .andExpect(status().isOk())
        .andExpect(view().name("employees/statistics"))
        .andExpect(model().attribute("totalEmployees", 5L))
        .andExpect(model().attributeExists("departmentStats"));
  }

  @Test
  void addEmployee_redirectsToListOnSuccess() throws Exception {
    mockMvc.perform(post("/employees/add")
        .param("name", "Nguyen Van A")
        .param("email", "a@example.com")
        .param("departmentId", "1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/employees/list"));

    verify(employeeService).createEmployee("Nguyen Van A", "a@example.com", 1L);
  }

  @Test
  void addEmployee_rendersFormAgainWhenValidationFails() throws Exception {
    mockMvc.perform(post("/employees/add")
        .param("name", "")
        .param("email", "not-an-email")
        .param("departmentId", ""))
        .andExpect(status().isOk())
        .andExpect(view().name("employees/add"))
        .andExpect(model().attributeHasFieldErrors("employeeForm", "name", "email"));

    verify(employeeService, never()).createEmployee(org.mockito.ArgumentMatchers.any(),
        org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
  }
}
