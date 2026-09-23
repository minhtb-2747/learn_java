package com.employee_management.employee_management.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UtilityServiceTest {

  private final UtilityService utilityService = new UtilityService();

  @Test
  void formatName_trimsAndUppercases() {
    assertThat(utilityService.formatName("  nguyen van a  ")).isEqualTo("NGUYEN VAN A");
  }

  @Test
  void generatedEmployeeCode_hasExpectedPrefix() {
    assertThat(utilityService.generatedEmployeeCode()).startsWith("E-");
  }
}
