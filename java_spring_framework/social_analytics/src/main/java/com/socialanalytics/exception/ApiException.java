package com.socialanalytics.exception;

import lombok.Getter;

// Lỗi nghiệp vụ do service chủ động throw, GlobalExceptionHandler chuyển thành response
@Getter
public class ApiException extends RuntimeException {

	private final ErrorCode errorCode;

	public ApiException(ErrorCode errorCode) {
		this(errorCode, errorCode.getDefaultMessage());
	}

	public ApiException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
