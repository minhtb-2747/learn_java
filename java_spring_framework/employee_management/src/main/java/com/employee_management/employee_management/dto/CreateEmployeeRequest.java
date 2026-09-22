package com.employee_management.employee_management.dto;

public record CreateEmployeeRequest(String name, String email, Long departmentId) {
}
