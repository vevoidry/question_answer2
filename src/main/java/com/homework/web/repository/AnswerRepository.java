package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	// 根据question_id返回多个Answer对象
	@Query(value = "select * from answer where question_id=:question_id", nativeQuery = true)
	List<Answer> findAllByQuestion_id(Integer question_id);

	// 根据question_id和分页参数返回多个Answer对象
	@Query(value = "select * from answer where question_id=:question_id limit :nonWantedSize,:wantedSize", nativeQuery = true)
	List<Answer> findAllByPage(Integer question_id, Integer nonWantedSize, Integer wantedSize);

	// 根据question_id返回多个Answer对象的总数量
	@Query(value = "select count(id) from answer where question_id=:question_id", nativeQuery = true)
	Integer findTheCountByQuestion_id(Integer question_id);

	// 根据question_id和user_id返回相应的一个Answer对象
	@Query(value = "select * from answer where user_id=:user_id and question_id=:question_id", nativeQuery = true)
	Answer findByUser_idQuestion_id(Integer user_id, Integer question_id);

	// 根据user_id返回最近的5个Answer
	@Query(value = "select * from answer where user_id=:user_id order by id desc limit 5", nativeQuery = true)
	List<Answer> findByUser_id(Integer user_id);

	// 根据question_id删除多个Answer对象
	@Modifying(clearAutomatically = true)
	@Query(value = "delete from answer where question_id=:question_id", nativeQuery = true)
	void deleteByQuestion_id(Integer question_id);
}
