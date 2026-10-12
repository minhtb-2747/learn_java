package com.socialanalytics.dto.request;

import org.hibernate.validator.constraints.URL;

import com.socialanalytics.entity.enums.Platform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SocialAccountRequest(
		@NotNull(message = "platform là bắt buộc")
		Platform platform,

		@NotBlank(message = "platformAccountId là bắt buộc")
		@Size(max = 100, message = "platformAccountId tối đa 100 ký tự")
		String platformAccountId,

		@NotBlank(message = "name là bắt buộc")
		@Size(max = 255, message = "name tối đa 255 ký tự")
		String name,

		@URL(message = "accountUrl không phải URL hợp lệ")
		@Size(max = 500, message = "accountUrl tối đa 500 ký tự")
		String accountUrl) {
}
