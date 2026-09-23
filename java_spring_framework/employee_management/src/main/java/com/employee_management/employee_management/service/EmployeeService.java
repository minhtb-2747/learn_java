package com.employee_management.employee_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.exception.EmployeeNotFoundException;
import com.employee_management.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

  private final UtilityService utilityService;
  private final EmployeeRepository employeeRepository;
  private final DepartmentService departmentService;

  public EmployeeService(UtilityService utilityService, EmployeeRepository employeeRepository,
      DepartmentService departmentService) {
    this.utilityService = utilityService;
    this.employeeRepository = employeeRepository;
    this.departmentService = departmentService;
  }

  public List<Employee> findAll() {
    return employeeRepository.findAll();
  }

  public Optional<Employee> findById(Long id) {
    return employeeRepository.findById(id);
  }

  public List<Employee> searchByName(String name) {
    return employeeRepository.findByNameContainingIgnoreCase(name);
  }

  public List<Employee> searchByDepartment(String departmentName) {
    return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
  }

  public Employee createEmployee(String name, String email, Long departmentId) {
    Department department = departmentService.findById(departmentId)
        .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + departmentId));

    Employee employee = new Employee(utilityService.formatName(name), email, department);

    return employeeRepository.save(employee);
  }

  public Employee updateEmployee(Long id, String name, String email, Long departmentId) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));

    Department department = departmentService.findById(departmentId)
        .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + departmentId));

    employee.setName(utilityService.formatName(name));
    employee.setEmail(email);
    employee.setDepartment(department);

    return employeeRepository.save(employee);
  }

  public void deleteEmployee(Long id) {
    if (!employeeRepository.existsById(id)) {
      throw new EmployeeNotFoundException(id);
    }

    employeeRepository.deleteById(id);
  }
}
