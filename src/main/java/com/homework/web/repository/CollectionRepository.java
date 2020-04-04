package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.Following;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {
	
	@Query(value = "select * from collection where user_id=:user_id ", nativeQuery = true)
	List<Collection> findAllByUser_id(Integer user_id);

	@Query(value = "select * from collection where user_id = :user_id and question_id=:question_id", nativeQuery = true)
	Collection findByUser_idQuestion_id(Integer user_id, Integer question_id);

	@Modifying(clearAutomatically = true)
	@Query(value = "delete from collection where question_id=:question_id", nativeQuery = true)
	void deleteByQuestion_id(Integer question_id);
}
