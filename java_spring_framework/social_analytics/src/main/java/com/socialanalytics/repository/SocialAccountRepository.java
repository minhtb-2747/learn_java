package com.socialanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialanalytics.entity.SocialAccount;
import com.socialanalytics.entity.enums.Platform;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

	boolean existsByPlatformAndPlatformAccountId(Platform platform, String platformAccountId);
}
