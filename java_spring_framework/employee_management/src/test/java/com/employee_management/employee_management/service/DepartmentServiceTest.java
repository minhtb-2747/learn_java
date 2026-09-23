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
import com.employee_management.employee_management.repository.DepartmentRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

  @Mock
  private DepartmentRepository departmentRepository;

  @InjectMocks
  private DepartmentService departmentService;

  @Test
  void findAll_returnsRepositoryResult() {
    List<Department> departments = List.of(new Department("IT"));
    when(departmentRepository.findAll()).thenReturn(departments);

    assertThat(departmentService.findAll()).isEqualTo(departments);
  }

  @Test
  void findById_delegatesToRepository() {
    Department department = new Department("IT");
    when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

    assertThat(departmentService.findById(1L)).contains(department);
  }

  @Test
  void create_savesDepartmentWithGivenName() {
    when(departmentRepository.save(any(Department.class))).thenAnswer(call -> call.getArgument(0));

    Department created = departmentService.create("IT");

    ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
    verify(departmentRepository).save(captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo("IT");
    assertThat(created.getName()).isEqualTo("IT");
  }

  @Test
  void update_changesNameOfExistingDepartment() {
    Department existing = new Department("IT");
    when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(departmentRepository.save(existing)).thenReturn(existing);

    Department updated = departmentService.update(1L, "Engineering");

    assertThat(updated.getName()).isEqualTo("Engineering");
  }

  @Test
  void update_throwsWhenDepartmentNotFound() {
    when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> departmentService.update(99L, "Engineering"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("99");

    verify(departmentRepository, never()).save(any());
  }

  @Test
  void delete_removesExistingDepartment() {
    when(departmentRepository.existsById(1L)).thenReturn(true);

    departmentService.delete(1L);

    verify(departmentRepository).deleteById(1L);
  }

  @Test
  void delete_throwsWhenDepartmentNotFound() {
    when(departmentRepository.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> departmentService.delete(99L))
        .isInstanceOf(IllegalArgumentException.class);

    verify(departmentRepository, never()).deleteById(any());
  }
}
