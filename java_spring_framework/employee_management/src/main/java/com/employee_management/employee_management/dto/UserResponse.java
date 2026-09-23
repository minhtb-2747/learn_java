package com.employee_management.employee_management.dto;

import com.employee_management.employee_management.Entity.Role;

public record UserResponse(Long id, String username, Role role) {
}
