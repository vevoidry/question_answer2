package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Answer;

public interface AnswerService {
	Answer save(Answer answer);

	List<Answer> findAllByPage(Integer question_id, Integer size, Integer page);

	Integer findTheCountByQuestion_id(Integer question_id);

	Answer findById(Integer id);

	Answer update(Answer answer);

	Answer findByUser_idQuestion_id(Integer user_id, Integer question_id);

	List<Answer> findByUser_id(Integer user_id);
	
	void deleteByQuestion_id(Integer question_id);
	
	void deleteById(Integer id);
}
