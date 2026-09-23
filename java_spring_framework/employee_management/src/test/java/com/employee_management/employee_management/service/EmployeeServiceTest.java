package com.employee_management.employee_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee_management.employee_management.Entity.Department;
import com.employee_management.employee_management.Entity.Employee;
import com.employee_management.employee_management.exception.EmployeeNotFoundException;
import com.employee_management.employee_management.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock
  private UtilityService utilityService;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private DepartmentService departmentService;

  @InjectMocks
  private EmployeeService employeeService;

  @Test
  void findAll_returnsRepositoryResult() {
    List<Employee> employees = List.of(new Employee("A", "a@example.com", new Department("IT")));
    when(employeeRepository.findAll()).thenReturn(employees);

    assertThat(employeeService.findAll()).isEqualTo(employees);
  }

  @Test
  void searchByName_usesCaseInsensitiveQuery() {
    List<Employee> employees = List.of(new Employee("A", "a@example.com", new Department("IT")));
    when(employeeRepository.findByNameContainingIgnoreCase("a")).thenReturn(employees);

    assertThat(employeeService.searchByName("a")).isEqualTo(employees);
  }

  @Test
  void countAll_returnsRepositoryCount() {
    when(employeeRepository.count()).thenReturn(7L);

    assertThat(employeeService.countAll()).isEqualTo(7L);
  }

  @Test
  void createEmployee_formatsNameAndResolvesDepartment() {
    Department department = new Department("IT");
    when(departmentService.findById(1L)).thenReturn(Optional.of(department));
    when(utilityService.formatName("nguyen van a")).thenReturn("NGUYEN VAN A");
    when(employeeRepository.save(any(Employee.class))).thenAnswer(call -> call.getArgument(0));

    employeeService.createEmployee("nguyen van a", "a@example.com", 1L);

    ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
    verify(employeeRepository).save(captor.capture());

    Employee saved = captor.getValue();
    assertThat(saved.getName()).isEqualTo("NGUYEN VAN A");
    assertThat(saved.getEmail()).isEqualTo("a@example.com");
    assertThat(saved.getDepartment()).isEqualTo(department);
  }

  @Test
  void createEmployee_throwsWhenDepartmentNotFound() {
    when(departmentService.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> employeeService.createEmployee("A", "a@example.com", 99L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("99");

    verify(employeeRepository, never()).save(any());
  }

  @Test
  void updateEmployee_overwritesFieldsOfExistingEmployee() {
    Department oldDepartment = new Department("IT");
    Department newDepartment = new Department("Sales");
    Employee existing = new Employee("OLD", "old@example.com", oldDepartment);

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(departmentService.findById(2L)).thenReturn(Optional.of(newDepartment));
    when(utilityService.formatName("new name")).thenReturn("NEW NAME");
    when(employeeRepository.save(existing)).thenReturn(existing);

    Employee updated = employeeService.updateEmployee(1L, "new name", "new@example.com", 2L);

    assertThat(updated.getName()).isEqualTo("NEW NAME");
    assertThat(updated.getEmail()).isEqualTo("new@example.com");
    assertThat(updated.getDepartment()).isEqualTo(newDepartment);
  }

  @Test
  void updateEmployee_throwsWhenEmployeeNotFound() {
    when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> employeeService.updateEmployee(99L, "A", "a@example.com", 1L))
        .isInstanceOf(EmployeeNotFoundException.class);

    verify(employeeRepository, never()).save(any());
  }

  @Test
  void deleteEmployee_removesExistingEmployee() {
    when(employeeRepository.existsById(1L)).thenReturn(true);

    employeeService.deleteEmployee(1L);

    verify(employeeRepository).deleteById(1L);
  }

  @Test
  void deleteEmployee_throwsWhenEmployeeNotFound() {
    when(employeeRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
        .isInstanceOf(EmployeeNotFoundException.class);

    verify(employeeRepository, never()).deleteById(any());
  }
}
