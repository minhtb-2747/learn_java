package com.employee_management.employee_management.controller;

import static com.employee_management.employee_management.support.TestUsers.AS_ADMIN;
import static com.employee_management.employee_management.support.TestUsers.AS_USER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.employee_management.employee_management.config.SecurityConfig;
import com.employee_management.employee_management.dto.DepartmentStatsResponse;
import com.employee_management.employee_management.security.JwtUtil;
import com.employee_management.employee_management.service.StatisticsService;

@WebMvcTest(StatisticsController.class)
@Import(SecurityConfig.class)
class StatisticsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StatisticsService statisticsService;

  @MockitoBean
  private JwtUtil jwtUtil;

  private static DepartmentStatsResponse stats(String departmentName, long employeeCount) {
    return new DepartmentStatsResponse() {

      @Override
      public String getDepartmentName() {
        return departmentName;
      }

      @Override
      public long getEmployeeCount() {
        return employeeCount;
      }
    };
  }

  @Test
  void byDepartment_returnsAggregatedCounts() throws Exception {
    when(statisticsService.countEmployeesByDepartment())
        .thenReturn(List.of(stats("IT", 3), stats("Sales", 1)));

    mockMvc.perform(get("/api/employees/statistics/by-department").with(AS_USER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].departmentName").value("IT"))
        .andExpect(jsonPath("$[0].employeeCount").value(3))
        .andExpect(jsonPath("$[1].departmentName").value("Sales"));
  }

  @Test
  void totalEmployees_returnsPlainNumber() throws Exception {
    when(statisticsService.countTotalEmployees()).thenReturn(12L);

    mockMvc.perform(get("/api/employees/statistics/total").with(AS_ADMIN))
        .andExpect(status().isOk())
        .andExpect(content().string("12"));
  }

  @Test
  void statistics_rejectedWhenNotAuthenticated() throws Exception {
    mockMvc.perform(get("/api/employees/statistics/total"))
        .andExpect(status().isForbidden());
  }
}
