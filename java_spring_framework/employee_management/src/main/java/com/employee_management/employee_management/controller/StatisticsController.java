package com.employee_management.employee_management.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.dto.DepartmentStatsResponse;
import com.employee_management.employee_management.service.StatisticsService;

@RestController
@RequestMapping("/api/employees/statistics")
public class StatisticsController {

  private final StatisticsService statisticsService;

  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/by-department")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<DepartmentStatsResponse> byDepartment() {
    return statisticsService.countEmployeesByDepartment();
  }

  @GetMapping("/total")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public long totalEmployees() {
    return statisticsService.countTotalEmployees();
  }
}
