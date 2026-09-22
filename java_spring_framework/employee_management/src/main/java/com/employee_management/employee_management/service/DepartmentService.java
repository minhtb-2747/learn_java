package com.employee_management.employee_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.repository.DepartmentRepository;

@Service
public class DepartmentService {
  private final DepartmentRepository departmentRepository;

  public DepartmentService(DepartmentRepository departmentRepository) {
    this.departmentRepository = departmentRepository;
  }

  public List<Department> findAll() {
    return departmentRepository.findAll();
  }

  public Optional<Department> findById(Long id) {
    return departmentRepository.findById(id);
  }

  public Department create(String name) {
    return departmentRepository.save(new Department(name));
  }

  public Department update(Long id, String name) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id));

    department.setName(name);

    return departmentRepository.save(department);
  }

  public void delete(Long id) {
    if (!departmentRepository.existsById(id)) {
      throw new IllegalArgumentException("Department not found with id: " + id);
    }

    departmentRepository.deleteById(id);
  }
}
