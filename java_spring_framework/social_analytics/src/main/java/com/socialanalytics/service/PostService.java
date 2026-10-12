package com.socialanalytics.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialanalytics.dto.request.PostCreateRequest;
import com.socialanalytics.dto.request.PostUpdateRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.PostResponse;
import com.socialanalytics.entity.Post;
import com.socialanalytics.entity.SocialAccount;
import com.socialanalytics.entity.enums.Platform;
import com.socialanalytics.exception.ApiException;
import com.socialanalytics.exception.ErrorCode;
import com.socialanalytics.repository.PostRepository;
import com.socialanalytics.util.StringUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	// Bài mới đăng lên trước
	private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id"));

	private final PostRepository postRepository;
	private final SocialAccountService socialAccountService;

	public PageResponse<PostResponse> search(Platform platform, Long socialAccountId, int page, int size) {
		PageRequest pageable = PageRequest.of(page - 1, size, DEFAULT_SORT);
		return PageResponse.from(postRepository.search(platform, socialAccountId, pageable), PostResponse::from);
	}

	public PostResponse findById(Long id) {
		return PostResponse.from(getPostWithAccount(id));
	}

	@Transactional
	public PostResponse create(PostCreateRequest request) {
		SocialAccount account = socialAccountService.getAccount(request.socialAccountId());
		String platformPostId = request.platformPostId().trim();
		if (postRepository.existsBySocialAccountIdAndPlatformPostId(account.getId(), platformPostId)) {
			throw new ApiException(ErrorCode.POST_ALREADY_EXISTS,
					"Bài viết " + platformPostId + " đã tồn tại trong social account id=" + account.getId());
		}

		Post post = new Post();
		post.setSocialAccount(account);
		post.setPlatformPostId(platformPostId);
		post.setPostUrl(StringUtil.blankToNull(request.postUrl()));
		post.setContent(request.content());
		post.setPublishedAt(request.publishedAt());
		// importedBy: gán từ user đang đăng nhập khi có Security

		Post saved = postRepository.save(post);
		log.info("Tạo post id={} (account id={}, platformPostId={})", saved.getId(), account.getId(), platformPostId);
		return PostResponse.from(saved);
	}

	@Transactional
	public PostResponse update(Long id, PostUpdateRequest request) {
		Post post = getPostWithAccount(id);
		post.setPostUrl(StringUtil.blankToNull(request.postUrl()));
		post.setContent(request.content());
		post.setPublishedAt(request.publishedAt());
		// flush để @UpdateTimestamp gán updatedAt trước khi map response
		postRepository.flush();
		log.info("Cập nhật post id={}", id);
		return PostResponse.from(post);
	}

	// Metrics của bài bị xóa theo nhờ ON DELETE CASCADE
	@Transactional
	public void delete(Long id) {
		postRepository.delete(getPost(id));
		log.info("Xóa post id={}", id);
	}

	// Dùng chung cho service khác cần post (vd SocialMetricService)
	Post getPost(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Không tìm thấy bài viết id=" + id));
	}

	// Chỉ kiểm tra tồn tại, không nạp entity
	void checkExists(Long id) {
		if (!postRepository.existsById(id)) {
			throw new ApiException(ErrorCode.NOT_FOUND, "Không tìm thấy bài viết id=" + id);
		}
	}

	private Post getPostWithAccount(Long id) {
		return postRepository.findWithAccountById(id)
				.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Không tìm thấy bài viết id=" + id));
	}
}
