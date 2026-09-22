package com.employee_management.employee_management.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private final PasswordEncoder passwordEncoder;
  private final UtilityService utilityService;

  public EmployeeService(UtilityService utilityService, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.utilityService = utilityService;
  }

  public String createEmployee(String name, String password) {
    String employeeCode = utilityService.generatedEmployeeCode();
    String employeeName = utilityService.formatName(name);
    String enCodedPassword = passwordEncoder.encode(password);

    return employeeCode + "| " + employeeName + "| " + enCodedPassword;
  }
}
