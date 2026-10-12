package com.socialanalytics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.socialanalytics.dto.request.PostCreateRequest;
import com.socialanalytics.dto.request.PostUpdateRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.PostResponse;
import com.socialanalytics.entity.enums.Platform;
import com.socialanalytics.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "Posts", description = "CRUD bài viết")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@Operation(summary = "Danh sách bài viết, lọc theo platform / socialAccountId (phân trang, page bắt đầu từ 1)")
	@GetMapping
	public PageResponse<PostResponse> search(
			@RequestParam(required = false) Platform platform,
			@RequestParam(required = false) Long socialAccountId,
			@RequestParam(defaultValue = "1") @Min(value = 1, message = "page phải >= 1") int page,
			@RequestParam(defaultValue = "10") @Min(value = 1, message = "size phải >= 1") @Max(value = 100, message = "size tối đa 100") int size) {
		return postService.search(platform, socialAccountId, page, size);
	}

	@Operation(summary = "Chi tiết bài viết")
	@GetMapping("/{id}")
	public PostResponse findById(@PathVariable Long id) {
		return postService.findById(id);
	}

	@Operation(summary = "Tạo bài viết (platform lấy theo social account)")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PostResponse create(@Valid @RequestBody PostCreateRequest request) {
		return postService.create(request);
	}

	@Operation(summary = "Cập nhật postUrl, content, publishedAt",
			description = "Thay thế toàn bộ: field không gửi sẽ thành null")
	@PutMapping("/{id}")
	public PostResponse update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
		return postService.update(id, request);
	}

	@Operation(summary = "Xóa bài viết (kèm toàn bộ metric của bài)")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		postService.delete(id);
	}
}
