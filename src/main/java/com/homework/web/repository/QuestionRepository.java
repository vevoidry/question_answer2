package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Question;
import com.homework.web.pojo.User;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	// 根据分页参数返回多个Question对象
	@Query(value = "select * from question where category_id!=0 limit :nonWantedSize,:wantedSize", nativeQuery = true)
	List<Question> findAllByPage(Integer nonWantedSize, Integer wantedSize);

	// 无参数返回Question对象的总数量
	@Query(value = "select count(id) from question where  category_id!=0 ", nativeQuery = true)
	Integer findTheCount();

	// 根据分页参数和关键词返回多个Question对象
	@Query(value = "select * from question where  category_id!=0 and title like %:data%  limit :nonWantedSize,:wantedSize ", nativeQuery = true)
	List<Question> findAllBySearch(String data, Integer nonWantedSize, Integer wantedSize);

	// 根据关键词返回Question对象的总数量
	@Query(value = "select count(id) from question where  category_id!=0 and title like %:data%  ", nativeQuery = true)
	Integer findTheCountBySearch(String data);

	// 根据排序返回五条最新数据
	@Query(value = "select * from question where  category_id!=0  order by id desc limit 5", nativeQuery = true)
	List<Question> findAllByLastLimit5();

	// 根据排序返回几条最新数据
	@Query(value = "select * from question where   category_id!=0   order by id desc limit :limit", nativeQuery = true)
	List<Question> findAllByLastLimit(Integer limit);

	// 根据user_id返回多个Question对象
	@Query(value = "select * from question where   category_id!=0  and user_id=:user_id", nativeQuery = true)
	List<Question> findAllByUser_id(Integer user_id);

	// 根据category_id和分页参数返回多个Question对象
	@Query(value = "select * from question where   category_id!=0  and category_id=:category_id  limit :nonWantedSize,:wantedSize ", nativeQuery = true)
	List<Question> findAllByCategory_idPage(Integer category_id, Integer nonWantedSize, Integer wantedSize);

	// 根据category_id返回question的总数量
	@Query(value = "select count(id) from question  where   category_id!=0  and category_id=:category_id ", nativeQuery = true)
	Integer findTheCountByCategory_id(Integer category_id);

	// 根据category_id将问题的category_id改为0
	@Modifying(clearAutomatically = true)
	@Query(value = "update question set category_id=0 where category_id!=0 and  category_id=:category_id", nativeQuery = true)
	void updateAllByCategory_id(Integer category_id);
}
