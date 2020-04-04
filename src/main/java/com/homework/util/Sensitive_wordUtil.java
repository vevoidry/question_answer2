package com.homework.util;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.homework.web.pojo.Sensitive_word;
import com.homework.web.service.impl.Sensitive_wordServiceImpl;

@Component
public class Sensitive_wordUtil {

	@Autowired
	private Sensitive_wordServiceImpl sensitive_wordServiceImpl;

	public String check(String input) {
		List<Sensitive_word> sensitive_wordList = sensitive_wordServiceImpl.findAll();
		Iterator<Sensitive_word> iterator = sensitive_wordList.iterator();
		while (iterator.hasNext()) {
			Sensitive_word sensitive_word = iterator.next();
			// 敏感词的替换字符串为长度与敏感词相同的*字符串
			String replaceString = "";
			for (int i = 0; i < sensitive_word.getWord().length(); i++) {
				replaceString += "*";
			}
			input = input.replaceAll(sensitive_word.getWord(), replaceString);
		}
		return input;
	}

}
