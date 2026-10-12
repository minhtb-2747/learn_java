package com.socialanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialanalytics.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
