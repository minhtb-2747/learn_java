package com.employee_management.employee_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.dto.CreateEmployeeRequest;
import com.employee_management.employee_management.dto.EmployeeResponse;
import com.employee_management.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public List<EmployeeResponse> findAll(@RequestParam(required = false) String name) {
    if (name != null) {
      return employeeService.searchByName(name);
    }

    return employeeService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> findById(@PathVariable Long id) {
    return employeeService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
    EmployeeResponse created = employeeService.createEmployee(request.name(), request.password());

    return ResponseEntity.ok(created);
  }
}
