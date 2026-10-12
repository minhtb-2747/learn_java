package com.socialanalytics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socialanalytics.dto.request.SocialMetricCreateRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.SocialMetricResponse;
import com.socialanalytics.service.SocialMetricService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

// Snapshot chỉ thêm/xóa, không có PUT
@Tag(name = "Metrics", description = "Snapshot chỉ số (likes, shares, comments) của bài viết")
@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class SocialMetricController {

	private final SocialMetricService socialMetricService;

	@Operation(summary = "Danh sách snapshot, lọc theo postId (mới nhất trước, page bắt đầu từ 1)")
	@GetMapping
	public PageResponse<SocialMetricResponse> findAll(
			@RequestParam(required = false) Long postId,
			@RequestParam(defaultValue = "1") @Min(value = 1, message = "page phải >= 1") int page,
			@RequestParam(defaultValue = "10") @Min(value = 1, message = "size phải >= 1") @Max(value = 100, message = "size tối đa 100") int size) {
		return socialMetricService.findAll(postId, page, size);
	}

	@Operation(summary = "Chi tiết snapshot")
	@GetMapping("/{id}")
	public SocialMetricResponse findById(@PathVariable Long id) {
		return socialMetricService.findById(id);
	}

	@Operation(summary = "Thêm snapshot cho bài viết")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SocialMetricResponse create(@Valid @RequestBody SocialMetricCreateRequest request) {
		return socialMetricService.create(request);
	}

	@Operation(summary = "Xóa snapshot")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		socialMetricService.delete(id);
	}
}
