package com.employee_management.employee_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeForm {

  @NotBlank(message = "Name must not be blank")
  private String name;

  @NotBlank(message = "Email must not be blank")
  @Email(message = "Email must be a valid email address")
  private String email;

  @NotNull(message = "Department id must not be null")
  private Long departmentId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }
}
