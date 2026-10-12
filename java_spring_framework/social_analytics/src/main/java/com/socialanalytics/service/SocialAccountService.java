package com.socialanalytics.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialanalytics.dto.request.SocialAccountRequest;
import com.socialanalytics.dto.response.PageResponse;
import com.socialanalytics.dto.response.SocialAccountResponse;
import com.socialanalytics.entity.SocialAccount;
import com.socialanalytics.exception.ApiException;
import com.socialanalytics.exception.ErrorCode;
import com.socialanalytics.repository.SocialAccountRepository;
import com.socialanalytics.util.StringUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialAccountService {

	private final SocialAccountRepository socialAccountRepository;

	public PageResponse<SocialAccountResponse> findAll(int page, int size) {
		PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
		return PageResponse.from(socialAccountRepository.findAll(pageable), SocialAccountResponse::from);
	}

	public SocialAccountResponse findById(Long id) {
		return SocialAccountResponse.from(getAccount(id));
	}

	@Transactional
	public SocialAccountResponse create(SocialAccountRequest request) {
		String platformAccountId = request.platformAccountId().trim();
		if (socialAccountRepository.existsByPlatformAndPlatformAccountId(request.platform(), platformAccountId)) {
			throw new ApiException(ErrorCode.SOCIAL_ACCOUNT_ALREADY_EXISTS,
					"Social account " + request.platform() + "/" + platformAccountId + " đã tồn tại");
		}

		SocialAccount account = new SocialAccount();
		account.setPlatform(request.platform());
		account.setPlatformAccountId(platformAccountId);
		account.setName(request.name().trim());
		account.setAccountUrl(StringUtil.blankToNull(request.accountUrl()));

		SocialAccount saved = socialAccountRepository.save(account);
		log.info("Tạo social account id={} ({}/{})", saved.getId(), saved.getPlatform(), saved.getPlatformAccountId());
		return SocialAccountResponse.from(saved);
	}

	// Dùng chung cho service khác cần account (vd PostService)
	SocialAccount getAccount(Long id) {
		return socialAccountRepository.findById(id)
				.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Không tìm thấy social account id=" + id));
	}
}
