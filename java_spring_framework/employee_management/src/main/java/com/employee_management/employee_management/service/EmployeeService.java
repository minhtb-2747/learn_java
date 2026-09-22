package com.employee_management.employee_management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee_management.employee_management.dto.EmployeeResponse;
import com.employee_management.employee_management.model.Employee;

@Service
public class EmployeeService {

  private final PasswordEncoder passwordEncoder;
  private final UtilityService utilityService;

  private final List<Employee> employees = new ArrayList<>();
  private final AtomicLong sequence = new AtomicLong();

  public EmployeeService(UtilityService utilityService, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.utilityService = utilityService;
  }

  public List<EmployeeResponse> findAll() {
    return employees.stream()
        .map(e -> new EmployeeResponse(e.id(), e.code(), e.name()))
        .toList();
  }

  public Optional<EmployeeResponse> findById(Long id) {
    return employees.stream()
        .filter(e -> e.id().equals(id))
        .findFirst()
        .map(e -> new EmployeeResponse(e.id(), e.code(), e.name()));
  }

  public List<EmployeeResponse> searchByName(String name) {
    String keyword = name.toLowerCase();

    return employees.stream()
        .filter(e -> e.name().toLowerCase().contains(keyword))
        .map(e -> new EmployeeResponse(e.id(), e.code(), e.name()))
        .toList();
  }

  public EmployeeResponse createEmployee(String name, String password) {
    Long id = sequence.incrementAndGet(); // id tăng dần
    String employeeCode = utilityService.generatedEmployeeCode();
    String employeeName = utilityService.formatName(name);
    String enCodedPassword = passwordEncoder.encode(password);

    Employee employee = new Employee(
        id,
        employeeCode,
        employeeName,
        enCodedPassword);

    employees.add(employee);

    return new EmployeeResponse(employee.id(), employee.code(), employee.name());
  }
}
