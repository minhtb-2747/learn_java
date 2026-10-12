package com.socialanalytics.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public record SocialMetricCreateRequest(
		@NotNull(message = "postId là bắt buộc")
		Long postId,

		@NotNull(message = "likes là bắt buộc")
		@PositiveOrZero(message = "likes phải >= 0")
		Long likes,

		@NotNull(message = "shares là bắt buộc")
		@PositiveOrZero(message = "shares phải >= 0")
		Long shares,

		@NotNull(message = "comments là bắt buộc")
		@PositiveOrZero(message = "comments phải >= 0")
		Long comments,

		@NotNull(message = "collectedAt là bắt buộc")
		@PastOrPresent(message = "collectedAt không được ở tương lai")
		Instant collectedAt) {
}
