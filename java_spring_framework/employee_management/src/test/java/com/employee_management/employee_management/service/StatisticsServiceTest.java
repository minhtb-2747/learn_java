package com.employee_management.employee_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee_management.employee_management.dto.DepartmentStatsResponse;
import com.employee_management.employee_management.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private EmployeeService employeeService;

  @InjectMocks
  private StatisticsService statisticsService;

  @Test
  void countEmployeesByDepartment_returnsRepositoryProjection() {
    DepartmentStatsResponse stats = mock(DepartmentStatsResponse.class);
    when(stats.getDepartmentName()).thenReturn("IT");
    when(stats.getEmployeeCount()).thenReturn(3L);
    when(employeeRepository.countEmployeesByDepartment()).thenReturn(List.of(stats));

    List<DepartmentStatsResponse> result = statisticsService.countEmployeesByDepartment();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getDepartmentName()).isEqualTo("IT");
    assertThat(result.get(0).getEmployeeCount()).isEqualTo(3L);
  }

  @Test
  void countTotalEmployees_reusesCachedEmployeeServiceCount() {
    when(employeeService.countAll()).thenReturn(12L);

    assertThat(statisticsService.countTotalEmployees()).isEqualTo(12L);
  }
}
