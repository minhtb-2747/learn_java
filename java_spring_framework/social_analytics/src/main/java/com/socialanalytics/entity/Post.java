package com.socialanalytics.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

// Bài viết; platform lấy qua socialAccount, metrics lấy qua SocialMetricRepository
@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "social_account_id", nullable = false)
	private SocialAccount socialAccount;

	@Column(name = "platform_post_id", nullable = false, length = 100)
	private String platformPostId;

	@Column(name = "post_url", length = 500)
	private String postUrl;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "published_at")
	private Instant publishedAt;

	// NULL khi bài do job crawl tạo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "imported_by")
	private User importedBy;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
}
