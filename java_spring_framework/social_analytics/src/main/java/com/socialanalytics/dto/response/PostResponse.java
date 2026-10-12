package com.socialanalytics.dto.response;

import java.time.Instant;

import com.socialanalytics.entity.Post;
import com.socialanalytics.entity.SocialAccount;
import com.socialanalytics.entity.User;
import com.socialanalytics.entity.enums.Platform;

// platform lấy từ social account
public record PostResponse(
		Long id,
		Platform platform,
		Long socialAccountId,
		String socialAccountName,
		String platformPostId,
		String postUrl,
		String content,
		Instant publishedAt,
		Long importedById,
		Instant createdAt,
		Instant updatedAt) {

	// Gọi trong @Transactional vì socialAccount là LAZY
	public static PostResponse from(Post post) {
		SocialAccount account = post.getSocialAccount();
		User importedBy = post.getImportedBy();
		return new PostResponse(post.getId(), account.getPlatform(), account.getId(), account.getName(),
				post.getPlatformPostId(), post.getPostUrl(), post.getContent(), post.getPublishedAt(),
				importedBy != null ? importedBy.getId() : null, post.getCreatedAt(), post.getUpdatedAt());
	}
}
