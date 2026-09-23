package com.employee_management.employee_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEmployeeRequest(
    @NotBlank(message = "Name must not be blank") String name,

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address") String email,

    @NotNull(message = "Department id must not be null") Long departmentId) {
}
