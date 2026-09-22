package com.employee_management.employee_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.dto.CreateEmployeeRequest;
import com.employee_management.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public List<Employee> findAll(@RequestParam(required = false) String name) {
    List<Employee> employees = name != null
        ? employeeService.searchByName(name)
        : employeeService.findAll();

    return employees;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> findById(@PathVariable Long id) {
    return employeeService.findById(id).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest request) {
    Employee created = employeeService.createEmployee(request.name(),
        request.email(), request.departmentId());

    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody CreateEmployeeRequest request) {
    Employee updated = employeeService.updateEmployee(id, request.name(),
        request.email(), request.departmentId());

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);

    return ResponseEntity.noContent().build();
  }

}
