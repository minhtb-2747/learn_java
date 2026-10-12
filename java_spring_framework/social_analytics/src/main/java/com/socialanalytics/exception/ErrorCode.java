package com.socialanalytics.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Mã lỗi của API: HTTP status + message mặc định
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy tài nguyên"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Phương thức HTTP không được hỗ trợ"),
	SOCIAL_ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Social account đã tồn tại"),
	POST_ALREADY_EXISTS(HttpStatus.CONFLICT, "Bài viết đã tồn tại trong social account này"),
	DATA_CONFLICT(HttpStatus.CONFLICT, "Dữ liệu vi phạm ràng buộc (trùng hoặc đang được tham chiếu)"),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type không được hỗ trợ"),
	VALIDATION(HttpStatus.UNPROCESSABLE_CONTENT, "Dữ liệu không hợp lệ"),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Đã có lỗi xảy ra, vui lòng thử lại sau");

	private final HttpStatus status;
	private final String defaultMessage;
}
