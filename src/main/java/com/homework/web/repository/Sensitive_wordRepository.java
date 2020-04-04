package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Sensitive_word;

public interface Sensitive_wordRepository extends JpaRepository<Sensitive_word, Integer> {
	@Query(value = "select * from sensitive_word where word = :word", nativeQuery = true)
	Sensitive_word findByWord(String word);
}
