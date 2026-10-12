package com.socialanalytics.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.socialanalytics.exception.ErrorCode;

// Format lỗi API: { "error": { code, message, fields } }
public record ErrorResponse(ErrorBody error) {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record ErrorBody(String code, String message, Map<String, String> fields) {
	}

	public static ErrorResponse of(ErrorCode code, String message) {
		return of(code, message, null);
	}

	public static ErrorResponse of(ErrorCode code, String message, Map<String, String> fields) {
		return new ErrorResponse(new ErrorBody(code.name(), message, fields));
	}
}
