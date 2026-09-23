package com.employee_management.employee_management.exception;

import java.util.List;

public record ErrorResponse(int status, String message, List<FieldError> errors) {

  public ErrorResponse(int status, String message) {
    this(status, message, null);
  }
}
