package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Sensitive_word;
import com.homework.web.repository.Sensitive_wordRepository;
import com.homework.web.service.Sensitive_wordService;

@Service
public class Sensitive_wordServiceImpl implements Sensitive_wordService {

	@Autowired
	private Sensitive_wordRepository sensitive_wordRepository;

	@Override
	public List<Sensitive_word> findAll() {
		return sensitive_wordRepository.findAll();
	}

	@Override
	public Sensitive_word findByWord(String word) {
		return sensitive_wordRepository.findByWord(word);
	}

	@Override
	public Sensitive_word save(Sensitive_word sensitive_word) {
		return sensitive_wordRepository.save(sensitive_word);
	}

	@Override
	public void deleteById(Integer id) {
		sensitive_wordRepository.deleteById(id);
	}

}
