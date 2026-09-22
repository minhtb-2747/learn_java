package com.employee_management.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee_management.employee_management.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
