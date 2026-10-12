package com.socialanalytics.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Snapshot chỉ số của bài viết tại một thời điểm, chỉ thêm không sửa
@Entity
@Table(name = "social_metrics")
@Getter
@Setter
@NoArgsConstructor
public class SocialMetric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@Column(nullable = false)
	private Long likes;

	@Column(nullable = false)
	private Long shares;

	@Column(nullable = false)
	private Long comments;

	@Column(name = "collected_at", nullable = false)
	private Instant collectedAt;
}
