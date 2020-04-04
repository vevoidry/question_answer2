package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Collection;

public interface CollectionService {

	Collection save(Collection collection);

	List<Collection> findAllByUser_id(Integer user_id);

	Collection findByUser_idQuestion_id(Integer user_id, Integer question_id);

	void deleteById(Integer id);
	
	void deleteByQuestion_id(Integer question_id);

}
