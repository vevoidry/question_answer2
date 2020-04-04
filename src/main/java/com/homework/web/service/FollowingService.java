package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Following;

public interface FollowingService {
	Following save(Following following);

	List<Following> findAllByFollowing_id(Integer following_id);

	List<Following> findAllByUser_id(Integer user_id);

	Following findByUser_idFollowing_id(Integer user_id, Integer following_id);

	void deleteById(Integer id);
}
