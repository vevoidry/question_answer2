package com.homework.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.util.JsonObject;
import com.homework.web.pojo.Answer;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.AnswerServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/answers")
public class AnswerController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AnswerServiceImpl answerServiceImpl;

	// 跳转至回答页面
	@GetMapping("/{question_id:[0-9]*}")
	public String getAnswerByQuestion_id(@PathVariable Integer question_id, Authentication authentication,
			Model model) {
		if (authentication != null) {
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			// 判断用户是否回答过该问题，若回答过该问题，则获取原回答数据
			System.out.println("111" + answerServiceImpl);
			Answer answer = answerServiceImpl.findByUser_idQuestion_id(me.getId(), question_id);
			System.out.println("222" + answer);
			if (answer != null) {
				model.addAttribute("answer", answer);
			}
		}
		model.addAttribute("question_id", question_id);
		// 跳转至回答页面
		return "answer";
	}

	// 提交回答
	@PostMapping
	@ResponseBody
	public JsonObject postApiAnswer(Answer answer, Authentication authentication) {
		// 获取已登录用户的信息
		if (authentication != null) {
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			// 判断该用户是否已经回答过该问题
			Answer answer3 = answerServiceImpl.findByUser_idQuestion_id(me.getId(), answer.getQuestion_id());
			if (answer3 != null) {
				// 若已回答过该问题，则更新原回答数据
				answer3.setContent(answer.getContent());
				answer3.setCreate_time(new Date());
				answerServiceImpl.update(answer3);
			} else {
				// 若未回答过该问题，则新增回答数据
				Answer answer2 = new Answer();
				answer2.setContent(answer.getContent());
				answer2.setUser_id(me.getId());
				answer2.setQuestion_id(answer.getQuestion_id());
				answerServiceImpl.save(answer2);
				// 为用户添加10点积分
				me.setScore(me.getScore() + 10);
				userServiceImpl.update(me);
			}
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("回复成功");
		jsonObject.setData("/questions/" + answer.getQuestion_id());
		// 跳转至问题页面
		return jsonObject;
	}

	@GetMapping("/delete")
	public String delete(Integer answer_id, @RequestParam(defaultValue = "1") Integer page,
			Authentication authentication) {
		// 获取已登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 获取该回答的信息
		Answer answer = answerServiceImpl.findById(answer_id);
		// 判断登录用户是否是回答的创建者或者管理员，若是则进行删除
		if (me.getId() == answer.getUser_id() || me.getRole().equals("admin")) {
			answerServiceImpl.deleteById(answer_id);
		}
		System.out.println(answer_id);
		return "redirect:/questions/" + answer.getQuestion_id() + "?page=" + page;
	}
}
