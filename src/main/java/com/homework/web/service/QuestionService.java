package com.homework.web.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.homework.web.pojo.Question;
import com.homework.web.pojo.User;

public interface QuestionService {
	Question save(Question question);

	List<Question> findAllByPage(Integer size, Integer page);

	Integer findTheCount();

	Question findById(Integer id);

	Question update(Question question);

	List<Question> findAllBySearch(String data,Integer size, Integer page);
	
	Integer findTheCountBySearch(String data);

	List<Question> findByRecommend(Authentication authentication);
	
	List<Question> findAllByLastLimit5();
	
	List<Question> findAllByLastLimit(Integer limit);
	
	List<Question> findAllByUser_id(Integer user_id);
	
	void deleteById(Integer id);
	
	List<Question> findAllByCategory_idPage(Integer category_id,Integer size, Integer page);
	
	Integer findTheCountByCategory_id(Integer category_id);
	
	void updateAllByCategory_id(Integer category_id);
	
}
