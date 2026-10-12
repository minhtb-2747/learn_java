package com.socialanalytics.dto.response;

import java.time.Instant;

import com.socialanalytics.entity.SocialMetric;

public record SocialMetricResponse(
		Long id,
		Long postId,
		Long likes,
		Long shares,
		Long comments,
		Instant collectedAt) {

	public static SocialMetricResponse from(SocialMetric metric) {
		return new SocialMetricResponse(metric.getId(), metric.getPost().getId(), metric.getLikes(),
				metric.getShares(), metric.getComments(), metric.getCollectedAt());
	}
}
