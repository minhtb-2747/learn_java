package com.socialanalytics.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.socialanalytics.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

// Xử lý exception tập trung cho toàn bộ API, trả về ErrorResponse
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApi(ApiException ex) {
		return build(ex.getErrorCode(), ex.getMessage(), null);
	}

	// Vi phạm unique/FK ở DB, vd 2 request tạo trùng cùng lúc
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
		log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
		return build(ErrorCode.DATA_CONFLICT, ErrorCode.DATA_CONFLICT.getDefaultMessage(), null);
	}

	// @RequestBody sai validation -> 422, @ModelAttribute -> 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleArgumentValidation(MethodArgumentNotValidException ex) {
		Map<String, String> fields = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fields.putIfAbsent(error.getField(),
						error.isBindingFailure() ? "Giá trị không hợp lệ" : error.getDefaultMessage()));

		boolean body = ex.getParameter().hasParameterAnnotation(RequestBody.class);
		ErrorCode code = body ? ErrorCode.VALIDATION : ErrorCode.BAD_REQUEST;
		return build(code, code.getDefaultMessage(), fields);
	}

	// Ràng buộc trên tham số controller (vd @Min(1) page)
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleMethodValidation(HandlerMethodValidationException ex) {
		Map<String, String> fields = new LinkedHashMap<>();
		boolean bodyInvalid = false;

		for (ParameterValidationResult result : ex.getParameterValidationResults()) {
			MethodParameter parameter = result.getMethodParameter();
			if (parameter.hasParameterAnnotation(RequestBody.class)) {
				bodyInvalid = true;
				if (result instanceof ParameterErrors errors) {
					errors.getFieldErrors()
							.forEach(error -> fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
				}
			} else if (!result.getResolvableErrors().isEmpty()) {
				fields.putIfAbsent(parameterName(parameter), result.getResolvableErrors().get(0).getDefaultMessage());
			}
		}

		ErrorCode code = bodyInvalid ? ErrorCode.VALIDATION : ErrorCode.BAD_REQUEST;
		return build(code, code.getDefaultMessage(), fields.isEmpty() ? null : fields);
	}

	// Sai kiểu tham số, vd page=abc, platform=ABC
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return build(ErrorCode.BAD_REQUEST, "Tham số '" + ex.getName() + "' không hợp lệ",
				Map.of(ex.getName(), "Giá trị không hợp lệ"));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
		return build(ErrorCode.BAD_REQUEST, "Thiếu tham số '" + ex.getParameterName() + "'",
				Map.of(ex.getParameterName(), "Bắt buộc"));
	}

	// JSON sai cú pháp, sai kiểu field hoặc enum không tồn tại
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
		return build(ErrorCode.BAD_REQUEST, "Request body không đúng định dạng JSON", null);
	}

	// Query string không giải mã được, vd chứa "%" sai -> 400 thay vì 500
	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<ErrorResponse> handleInvalidParameter(InvalidParameterException ex) {
		return build(ErrorCode.BAD_REQUEST, "Tham số trên URL không đúng định dạng", null);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException ex) {
		return build(ErrorCode.NOT_FOUND, "Không tìm thấy đường dẫn", null);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		return build(ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getDefaultMessage(), null);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		return build(ErrorCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE.getDefaultMessage(), null);
	}

	// Lỗi ngoài dự kiến: log đủ stack trace, client chỉ nhận message chung
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex);
		return build(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getDefaultMessage(), null);
	}

	private ResponseEntity<ErrorResponse> build(ErrorCode code, String message, Map<String, String> fields) {
		return ResponseEntity
				.status(code.getStatus())
				.body(ErrorResponse.of(code, message, fields));
	}

	// Ưu tiên tên trong @RequestParam/@PathVariable, không có thì dùng tên biến Java
	private static String parameterName(MethodParameter parameter) {
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		if (requestParam != null && !requestParam.name().isEmpty()) {
			return requestParam.name();
		}
		PathVariable pathVariable = parameter.getParameterAnnotation(PathVariable.class);
		if (pathVariable != null && !pathVariable.name().isEmpty()) {
			return pathVariable.name();
		}
		return parameter.getParameterName();
	}
}
