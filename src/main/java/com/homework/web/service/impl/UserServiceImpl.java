package com.homework.web.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.User;
import com.homework.web.repository.UserRepository;
import com.homework.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) {
		Date date = new Date();
		user.setRole("vip");
		user.setNickname(date.getTime() + "");
		user.setImage("x.png");
		user.setGender("男");
		user.setProfile("此人没有写任何简介");
		user.setScore(0);
		user.setIs_using(true);
		return userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByNickname(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	@Override
	public String findNicknameById(Integer id) {
		return userRepository.findNicknameById(id);
	}

	@Override
	public List<User> findAllByPage(Integer nonWantedSize, Integer wantedSize) {
		return userRepository.findAllByPage(nonWantedSize, wantedSize);
	}

	@Override
	public Integer findTheCount() {
		return userRepository.findTheCount();
	}

	@Override
	public User findById(Integer id) {
		return userRepository.findById(id).get();
	}

	@Override
	public List<User> findTenMostScoreUser() {
		return userRepository.findTenMostScoreUser();
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

}
