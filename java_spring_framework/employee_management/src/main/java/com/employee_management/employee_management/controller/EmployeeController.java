package com.employee_management.employee_management.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.dto.CreateEmployeeRequest;
import com.employee_management.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  public String createEmployee(@RequestBody CreateEmployeeRequest request) {
    return employeeService.createEmployee(request.name(), request.password());
  }

}
