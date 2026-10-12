package com.socialanalytics.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialanalytics.dto.request.SocialMetricCreateRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.SocialMetricResponse;
import com.socialanalytics.entity.Post;
import com.socialanalytics.entity.SocialMetric;
import com.socialanalytics.exception.ApiException;
import com.socialanalytics.exception.ErrorCode;
import com.socialanalytics.repository.SocialMetricRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialMetricService {

	// Snapshot mới nhất lên trước
	private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.desc("collectedAt"), Sort.Order.desc("id"));

	private final SocialMetricRepository socialMetricRepository;
	private final PostService postService;

	// postId null -> tất cả snapshot
	public PageResponse<SocialMetricResponse> findAll(Long postId, int page, int size) {
		PageRequest pageable = PageRequest.of(page - 1, size, DEFAULT_SORT);
		Page<SocialMetric> result;
		if (postId == null) {
			result = socialMetricRepository.findAll(pageable);
		} else {
			postService.checkExists(postId);
			result = socialMetricRepository.findByPostId(postId, pageable);
		}
		return PageResponse.from(result, SocialMetricResponse::from);
	}

	public SocialMetricResponse findById(Long id) {
		return SocialMetricResponse.from(getMetric(id));
	}

	@Transactional
	public SocialMetricResponse create(SocialMetricCreateRequest request) {
		Post post = postService.getPost(request.postId());

		SocialMetric metric = new SocialMetric();
		metric.setPost(post);
		metric.setLikes(request.likes());
		metric.setShares(request.shares());
		metric.setComments(request.comments());
		metric.setCollectedAt(request.collectedAt());

		SocialMetric saved = socialMetricRepository.save(metric);
		log.info("Thêm metric id={} cho post id={}", saved.getId(), post.getId());
		return SocialMetricResponse.from(saved);
	}

	@Transactional
	public void delete(Long id) {
		socialMetricRepository.delete(getMetric(id));
		log.info("Xóa metric id={}", id);
	}

	private SocialMetric getMetric(Long id) {
		return socialMetricRepository.findById(id)
				.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Không tìm thấy metric id=" + id));
	}
}
