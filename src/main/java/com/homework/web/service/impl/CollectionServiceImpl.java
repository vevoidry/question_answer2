package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.web.pojo.Collection;
import com.homework.web.repository.CollectionRepository;
import com.homework.web.service.CollectionService;

@Service
public class CollectionServiceImpl implements CollectionService {

	@Autowired
	private CollectionRepository collectionRepository;

	@Override
	public Collection save(Collection collection) {
		return collectionRepository.save(collection);
	}

	@Override
	public List<Collection> findAllByUser_id(Integer user_id) {
		return collectionRepository.findAllByUser_id(user_id);
	}

	@Override
	public Collection findByUser_idQuestion_id(Integer user_id, Integer question_id) {
		return collectionRepository.findByUser_idQuestion_id(user_id, question_id);
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		collectionRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void deleteByQuestion_id(Integer question_id) {
		collectionRepository.deleteByQuestion_id(question_id);
	}

}
