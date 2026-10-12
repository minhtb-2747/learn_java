package com.socialanalytics.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socialanalytics.entity.Post;
import com.socialanalytics.entity.enums.Platform;

public interface PostRepository extends JpaRepository<Post, Long> {

	@EntityGraph(attributePaths = "socialAccount")
	@Query("""
			select p from Post p
			where (:platform is null or p.socialAccount.platform = :platform)
			and (:socialAccountId is null or p.socialAccount.id = :socialAccountId)
			""")
	Page<Post> search(@Param("platform") Platform platform, @Param("socialAccountId") Long socialAccountId,
			Pageable pageable);

	@EntityGraph(attributePaths = "socialAccount")
	Optional<Post> findWithAccountById(Long id);

	boolean existsBySocialAccountIdAndPlatformPostId(Long socialAccountId, String platformPostId);
}
