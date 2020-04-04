package com.homework.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Answer;
import com.homework.web.pojo.User;
import com.homework.web.pojo.Vote;
import com.homework.web.service.impl.AnswerServiceImpl;
import com.homework.web.service.impl.QuestionServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;
import com.homework.web.service.impl.VoteServiceImpl;

@Controller
@RequestMapping("/votes")
public class VoteController {
	@Autowired
	private VoteServiceImpl voteServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	@Autowired
	private AnswerServiceImpl answerServiceImpl;

	@GetMapping
	public String getVotes(Integer answer_id, Integer page, Authentication authentication) {
		// 获取点赞的回答的数据
		Answer answer = answerServiceImpl.findById(answer_id);
		// 判断用户是否已登录
		if (authentication != null) {
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			Vote vote = voteServiceImpl.findByAnswer_idUser_id(answer_id, me.getId());
			// 判断用户是否已经点过赞，若未点赞，则效果为点赞
			if (vote == null) {
				Vote vote2 = new Vote();
				vote2.setAnswer_id(answer_id);
				vote2.setUser_id(me.getId());
				voteServiceImpl.save(vote2);
				// 该回答的赞数+1
				answer.setVote_count(answer.getVote_count() + 1);
				answerServiceImpl.update(answer);
			} else {
				// 若点过赞，则效果为取消点赞
				voteServiceImpl.deleteById(vote.getId());
				// 该回答的赞数-1
				answer.setVote_count(answer.getVote_count() - 1);
				answerServiceImpl.update(answer);
			}
		}
		// 跳转至问题页面
		return "redirect:questions/" + answer.getQuestion_id() + "?page=" + page;
	}
}
