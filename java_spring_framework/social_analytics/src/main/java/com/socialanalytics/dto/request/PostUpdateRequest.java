package com.socialanalytics.dto.request;

import java.time.Instant;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

// socialAccountId và platformPostId là định danh của bài nên không cho sửa
public record PostUpdateRequest(
		@URL(message = "postUrl không phải URL hợp lệ")
		@Size(max = 500, message = "postUrl tối đa 500 ký tự")
		String postUrl,

		String content,

		@PastOrPresent(message = "publishedAt không được ở tương lai")
		Instant publishedAt) {
}
