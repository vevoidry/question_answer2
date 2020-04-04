package com.homework.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import com.homework.util.Sensitive_wordUtil;
import com.homework.web.pojo.Answer;
import com.homework.web.pojo.Category;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.Question;
import com.homework.web.pojo.Sensitive_word;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.AnswerServiceImpl;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.CollectionServiceImpl;
import com.homework.web.service.impl.QuestionServiceImpl;
import com.homework.web.service.impl.Sensitive_wordServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AnswerServiceImpl answerServiceImpl;
	@Autowired
	private CollectionServiceImpl collectionServiceImpl;
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// 跳转至提问页面
	@GetMapping
	public String getQuestion(Model model) {
		// 获取所有分类
		List<Category> categoryList = categoryServiceImpl.findAll();
		model.addAttribute("categoryList", categoryList);
		return "postQuestion";
	}

	// 进行提问
	@PostMapping
	@ResponseBody
	public JsonObject postQuestion(Question question, Authentication authentication, String tags) {
		// 获取登录用户的信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 设置question的user_id后保存数据
		question.setUser_id(me.getId());
		Question question2 = questionServiceImpl.save(question);
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("提问成功");
		jsonObject.setData("/questions/" + question2.getId());
		// 跳转至新提问的问题页面
		return jsonObject;
	}

	// 跳转至问题页面
	@GetMapping("/{id:[0-9]*}")
	public String getQuestionById(@PathVariable Integer id, Model model, Authentication authentication,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "1") Integer page) {
		// 判断用户是否已登录
		if (authentication != null) {
			// 若已登录则判断用户是否已经关注该问题
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			Collection collection = collectionServiceImpl.findByUser_idQuestion_id(me.getId(), id);
			// 是否已经关注该问题
			if (collection != null) {
				model.addAttribute("hasCollection", true);
			} else {
				model.addAttribute("hasCollection", false);
			}
		} else {
			// 未登录则直接设置未关注该问题
			model.addAttribute("hasCollection", false);
		}
		// 获取当前Question及其User
		HashMap<Question, User> questionUserMap = new HashMap<Question, User>();
		Question question = questionServiceImpl.findById(id);
		User questionUser = userServiceImpl.findById(question.getUser_id());
		questionUserMap.put(question, questionUser);
		// 获取该question下的answer的分页
		Integer answerCount = answerServiceImpl.findTheCountByQuestion_id(id);
		Integer pageCount = (answerCount - 1) / size + 1;
		List<Integer> pageList = new ArrayList<Integer>();
		for (int i = 1; i <= pageCount; i++) {
			pageList.add(i);
		}
		// 获取该分页下的answer及其User
		HashMap<Answer, User> answerUserMap = new HashMap<Answer, User>();
		List<Answer> answerList = answerServiceImpl.findAllByPage(id, size, page);
		if (answerList != null) {
			Iterator<Answer> answerIterator = answerList.iterator();
			while (answerIterator.hasNext()) {
				Answer answer = answerIterator.next();
				if (answer != null) {
					User answerUser = userServiceImpl.findById(answer.getUser_id());
					answerUserMap.put(answer, answerUser);
				}
			}
		}
		// 获取置顶回答及其User
		if (question.getAnswer_id() != null) {
			HashMap<Answer, User> topAnswerUserMap = new HashMap<Answer, User>();
			Answer topAnswer = answerServiceImpl.findById(question.getAnswer_id());
			User topAnserUser = userServiceImpl.findById(topAnswer.getUser_id());
			topAnswerUserMap.put(topAnswer, topAnserUser);
			// 将数据放入model中
			model.addAttribute("topAnswerUserMap", topAnswerUserMap);
		}
		// 获取该提问的分类
		Category category = categoryServiceImpl.findById(question.getCategory_id());
		// 将其余数据放入model中
		model.addAttribute("questionUserMap", questionUserMap);
		model.addAttribute("pageList", pageList);
		model.addAttribute("answerUserMap", answerUserMap);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		return "question";
	}

	// 关闭问题的回答功能
	@GetMapping("/{id:[0-9]*}/close")
	public String getQuestionByIdClose(@PathVariable Integer id, Boolean on_off, Authentication authentication) {
		// 获取已登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		Question question = questionServiceImpl.findById(id);
		// 判断该问题是否由已登录用户提出，若是，则关闭问题。若登录用户是管理员，也有相同效果
		if (me.getId() == question.getUser_id() || me.getRole().equals("admin")) {
			question.setOn_off(on_off);
			questionServiceImpl.update(question);
		}
		// 重定向至问题页面
		return "redirect:/questions/" + id;
	}

	// 对某个回答进行置顶
	@GetMapping("/{question_id:[0-9]*}/top/{answer_id:[0-9]*}")
	public String top(@PathVariable Integer question_id, @PathVariable Integer answer_id,
			Authentication authentication) {
		// 获取已登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		Question question = questionServiceImpl.findById(question_id);
		// 判断该问题是否由已登录用户提出，若是，则对某个回答置顶。若是管理员，也有相同效果
		if (me.getId() == question.getUser_id() || me.getRole().equals("admin")) {
			question.setAnswer_id(answer_id);
			questionServiceImpl.update(question);
			// 对置顶回答的用户添加积分
			Answer answer = answerServiceImpl.findById(answer_id);
			User user = userServiceImpl.findById(answer.getUser_id());
			user.setScore(user.getScore() + question.getScore());
			userServiceImpl.update(user);
		}
		// 跳转至问题页面
		return "redirect:/questions/" + question_id;
	}

	// 删除问题
	@GetMapping("/delete")
	public String deleteById(Integer question_id, Authentication authentication) {
		// 获取已登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 获取该提问的信息
		Question question = questionServiceImpl.findById(question_id);
		// 判断登录用户是否是问题的创建者或者管理员，若是则进行删除
		if (me.getId() == question.getUser_id() || me.getRole().equals("admin")) {
			// 删除提问
			questionServiceImpl.deleteById(question_id);
		}
		return "redirect:/";
	}

}
