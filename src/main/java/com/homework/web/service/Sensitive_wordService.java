package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Sensitive_word;

public interface Sensitive_wordService {
	List<Sensitive_word> findAll();
	
	Sensitive_word findByWord(String word);
	
	Sensitive_word save(Sensitive_word sensitive_word);
	
	void deleteById(Integer id);
}
