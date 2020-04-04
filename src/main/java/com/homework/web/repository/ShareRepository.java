package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Share;

public interface ShareRepository extends JpaRepository<Share, Integer> {
	@Query(value = "select * from share where user_id=:user_id", nativeQuery = true)
	List<Share> findAllByUser_id(Integer user_id);
}
