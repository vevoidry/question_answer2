package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.util.JsonObject;
import com.homework.web.pojo.Sensitive_word;
import com.homework.web.service.impl.Sensitive_wordServiceImpl;

@Controller
@RequestMapping("/sensitive_words")
public class Sensitive_wordController {
	@Autowired
	private Sensitive_wordServiceImpl sensitive_wordServiceImpl;

	// 添加敏感词
	@PostMapping
	@ResponseBody
	public JsonObject postSensitive_word(String word) {
		// 判断输入栏的数据是否为空
		if (word == null || word.trim().equals("")) {
			throw new RuntimeException("敏感词不可为空");
		}
		word = word.trim();
		// 判断该敏感词是否已经存在
		Sensitive_word sensitive_word = sensitive_wordServiceImpl.findByWord(word);
		if (sensitive_word != null) {
			throw new RuntimeException("该敏感词已存在");
		} else {
			Sensitive_word sensitive_word2 = new Sensitive_word();
			sensitive_word2.setWord(word);
			sensitive_wordServiceImpl.save(sensitive_word2);
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("添加成功");
		jsonObject.setData("/admin_sensitive_word");
		return jsonObject;
	}

	// 移除相应敏感词
	@GetMapping("{id:[0-9]*}")
	public String get(@PathVariable Integer id) {
		sensitive_wordServiceImpl.deleteById(id);
		return "redirect:/admin_sensitive_word";
	}

}
