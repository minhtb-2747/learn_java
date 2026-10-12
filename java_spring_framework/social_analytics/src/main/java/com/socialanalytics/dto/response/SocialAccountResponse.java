package com.socialanalytics.dto.response;

import java.time.Instant;

import com.socialanalytics.entity.SocialAccount;
import com.socialanalytics.entity.enums.Platform;

public record SocialAccountResponse(
		Long id,
		Platform platform,
		String platformAccountId,
		String name,
		String accountUrl,
		Long followers,
		Instant followersUpdatedAt,
		Instant createdAt,
		Instant updatedAt) {

	public static SocialAccountResponse from(SocialAccount account) {
		return new SocialAccountResponse(account.getId(), account.getPlatform(), account.getPlatformAccountId(),
				account.getName(), account.getAccountUrl(), account.getFollowers(), account.getFollowersUpdatedAt(),
				account.getCreatedAt(), account.getUpdatedAt());
	}
}
