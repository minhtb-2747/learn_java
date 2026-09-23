package com.employee_management.employee_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.dto.DepartmentStatsResponse;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByNameContainingIgnoreCase(String name);

  List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

  @Query("SELECT e.department.name AS departmentName, COUNT(e) AS employeeCount "
      + "FROM Employee e GROUP BY e.department.name")
  List<DepartmentStatsResponse> countEmployeesByDepartment();

}
