package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
	@Query(value = "select * from vote where answer_id = :answer_id", nativeQuery = true)
	List<Vote> findByAnswer_id(Integer answer_id);

	@Query(value = "select * from vote where answer_id = :answer_id and user_id=:user_id", nativeQuery = true)
	Vote findByAnswer_idUser_id(Integer answer_id, Integer user_id);

	@Modifying(clearAutomatically = true)
	@Query(value = "delete from vote where answer_id=:answer_id", nativeQuery = true)
	void deleteByAnswer_id(Integer answer_id);
}
