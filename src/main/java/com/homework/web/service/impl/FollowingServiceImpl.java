package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.web.pojo.Following;
import com.homework.web.repository.FollowingRepository;
import com.homework.web.service.FollowingService;

@Service
public class FollowingServiceImpl implements FollowingService {
	@Autowired
	private FollowingRepository followingRepository;

	@Override
	public Following save(Following following) {
		return followingRepository.save(following);
	}

	@Override
	public List<Following> findAllByFollowing_id(Integer following_id) {
		return followingRepository.findAllByFollowing_id(following_id);
	}

	@Override
	public List<Following> findAllByUser_id(Integer user_id) {
		return followingRepository.findAllByUser_id(user_id);
	}

	@Override
	public Following findByUser_idFollowing_id(Integer user_id, Integer following_id) {
		return followingRepository.findByUser_idFollowing_id(user_id, following_id);
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		followingRepository.deleteById(id);
	}

}
