package com.employee_management.employee_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.employee_management.employee_management.dto.DepartmentStatsResponse;
import com.employee_management.employee_management.repository.EmployeeRepository;

@Service
public class StatisticsService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeService employeeService;

  public StatisticsService(EmployeeRepository employeeRepository, EmployeeService employeeService) {
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
  }

  public List<DepartmentStatsResponse> countEmployeesByDepartment() {
    return employeeRepository.countEmployeesByDepartment();
  }

  public long countTotalEmployees() {
    return employeeService.countAll();
  }
}
