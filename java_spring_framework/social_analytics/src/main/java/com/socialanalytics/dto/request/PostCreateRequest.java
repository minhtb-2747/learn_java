package com.socialanalytics.dto.request;

import java.time.Instant;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

// platform lấy theo social account
public record PostCreateRequest(
		@NotNull(message = "socialAccountId là bắt buộc")
		Long socialAccountId,

		@NotBlank(message = "platformPostId là bắt buộc")
		@Size(max = 100, message = "platformPostId tối đa 100 ký tự")
		String platformPostId,

		@URL(message = "postUrl không phải URL hợp lệ")
		@Size(max = 500, message = "postUrl tối đa 500 ký tự")
		String postUrl,

		String content,

		@PastOrPresent(message = "publishedAt không được ở tương lai")
		Instant publishedAt) {
}
