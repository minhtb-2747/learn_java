package com.employee_management.employee_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.dto.DepartmentRequest;
import com.employee_management.employee_management.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

  private final DepartmentService departmentService;

  public DepartmentController(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<Department> findAll() {
    return departmentService.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<Department> findById(@PathVariable Long id) {
    return departmentService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Department> create(@RequestBody DepartmentRequest request) {
    Department created = departmentService.create(request.name());

    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody DepartmentRequest request) {
    Department updated = departmentService.update(id, request.name());

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    departmentService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
