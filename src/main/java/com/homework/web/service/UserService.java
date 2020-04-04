package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.User;

public interface UserService {
	User save(User user);
	
	User findById(Integer id);
	
	User findByUsername(String username);
	
	User findByNickname(String nickname);
	
	String findNicknameById(Integer id);
	
	List<User> findAllByPage(Integer nonWantedSize, Integer wantedSize);
	
	Integer findTheCount();
	
	List<User> findTenMostScoreUser();
	
	User update(User user);
	
	List<User> findAll();
}
