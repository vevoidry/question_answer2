package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Following;

public interface FollowingRepository extends JpaRepository<Following, Integer> {
	@Query(value = "select * from following where following_id = :following_id", nativeQuery = true)
	List<Following> findAllByFollowing_id(Integer following_id);

	@Query(value = "select * from following where user_id = :user_id", nativeQuery = true)
	List<Following> findAllByUser_id(Integer user_id);

	@Query(value = "select * from following where user_id = :user_id and following_id=:following_id", nativeQuery = true)
	Following findByUser_idFollowing_id(Integer user_id, Integer following_id);
}
