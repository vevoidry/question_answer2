package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	// 根据username返回一个User对象
	@Query(value = "select * from user where username = :username", nativeQuery = true)
	User findByUsername(String username);

	// 根据nickname返回一个User对象
	@Query(value = "select * from user where nickname = :nickname", nativeQuery = true)
	User findByNickname(String nickname);

	// 根据nickname返回一个User对象的id
	@Query(value = "select nickname from user where id = :id", nativeQuery = true)
	String findNicknameById(Integer id);

	// 根据分页参数返回多个User对象
	@Query(value = "select * from user limit :nonWantedSize,:wantedSize", nativeQuery = true)
	List<User> findAllByPage(Integer nonWantedSize, Integer wantedSize);

	// 无参数返回User对象的总数量
	@Query(value = "select count(id) from user", nativeQuery = true)
	Integer findTheCount();

	// 返回积分最高的前十个用户，且按顺序排列
	@Query(value = "select * from user order by score desc limit 10", nativeQuery = true)
	List<User> findTenMostScoreUser();
}
