package com.socialanalytics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socialanalytics.dto.request.SocialAccountRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.SocialAccountResponse;
import com.socialanalytics.service.SocialAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "Social Accounts", description = "Trang Facebook / tài khoản Twitter đang theo dõi")
@RestController
@RequestMapping("/api/v1/social-accounts")
@RequiredArgsConstructor
public class SocialAccountController {

	private final SocialAccountService socialAccountService;

	@Operation(summary = "Danh sách social account (phân trang, page bắt đầu từ 1)")
	@GetMapping
	public PageResponse<SocialAccountResponse> findAll(
			@RequestParam(defaultValue = "1") @Min(value = 1, message = "page phải >= 1") int page,
			@RequestParam(defaultValue = "10") @Min(value = 1, message = "size phải >= 1") @Max(value = 100, message = "size tối đa 100") int size) {
		return socialAccountService.findAll(page, size);
	}

	@Operation(summary = "Chi tiết social account")
	@GetMapping("/{id}")
	public SocialAccountResponse findById(@PathVariable Long id) {
		return socialAccountService.findById(id);
	}

	@Operation(summary = "Tạo social account")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SocialAccountResponse create(@Valid @RequestBody SocialAccountRequest request) {
		return socialAccountService.create(request);
	}
}
