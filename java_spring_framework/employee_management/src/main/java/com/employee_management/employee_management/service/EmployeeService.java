package com.employee_management.employee_management.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.exception.EmployeeNotFoundException;
import com.employee_management.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

  private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

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

  @Cacheable("employeeCount")
  public long countAll() {
    log.info("Counting all employees");

    return employeeRepository.count();
  }

  public Employee createEmployee(String name, String email, Long departmentId) {
    log.info("Creating employee: name={}, email={}, departmentId={}", name, email, departmentId);

    Department department = departmentService.findById(departmentId)
        .orElseThrow(() -> {
          log.warn("Create employee failed: department not found, departmentId={}", departmentId);
          return new IllegalArgumentException("Department not found with id: " + departmentId);
        });

    Employee employee = new Employee(utilityService.formatName(name), email, department);
    Employee saved = employeeRepository.save(employee);

    log.info("Created employee: id={}, name={}", saved.getId(), saved.getName());

    return saved;
  }

  public Employee updateEmployee(Long id, String name, String email, Long departmentId) {
    log.info("Updating employee: id={}, name={}, email={}, departmentId={}", id, name, email, departmentId);

    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("Update employee failed: employee not found, id={}", id);
          return new EmployeeNotFoundException(id);
        });

    Department department = departmentService.findById(departmentId)
        .orElseThrow(() -> {
          log.warn("Update employee failed: department not found, departmentId={}", departmentId);
          return new IllegalArgumentException("Department not found with id: " + departmentId);
        });

    employee.setName(utilityService.formatName(name));
    employee.setEmail(email);
    employee.setDepartment(department);

    Employee saved = employeeRepository.save(employee);

    log.info("Updated employee: id={}, name={}", saved.getId(), saved.getName());

    return saved;
  }

  public void deleteEmployee(Long id) {
    log.info("Deleting employee: id={}", id);

    if (!employeeRepository.existsById(id)) {
      log.warn("Delete employee failed: employee not found, id={}", id);
      throw new EmployeeNotFoundException(id);
    }

    employeeRepository.deleteById(id);

    log.info("Deleted employee: id={}", id);
  }
}
