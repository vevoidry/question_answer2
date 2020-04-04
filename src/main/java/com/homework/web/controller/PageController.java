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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.util.Sensitive_wordUtil;
import com.homework.web.pojo.Category;
import com.homework.web.pojo.Question;
import com.homework.web.pojo.Sensitive_word;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.QuestionServiceImpl;
import com.homework.web.service.impl.Sensitive_wordServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
public class PageController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	@Autowired
	private Sensitive_wordServiceImpl sensitive_wordServiceImpl;
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	// 主页
	@GetMapping("/")
	public String index(Authentication authentication, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "1") Integer page, Model model) {
		// 所有问题的分页数据
		Integer questionCount = questionServiceImpl.findTheCount();
		Integer pageCount = (questionCount - 1) / size + 1;
		List<Integer> pageList = new ArrayList<Integer>();
		for (int i = 1; i <= pageCount; i++) {
			pageList.add(i);
		}
		// 当前分页的问题数据
		List<Question> questionList = questionServiceImpl.findAllByPage(size, page);
		// 积分排行榜的数据
		List<User> tenMostScoreUserList = userServiceImpl.findTenMostScoreUser();
		// 推荐问题的数据
		List<Question> recommendQuestionList = questionServiceImpl.findByRecommend(authentication);
		// 所有分类的数据
		List<Category> categoryList = categoryServiceImpl.findAll();
		// 将所有数据都放入model中
		model.addAttribute("pageList", pageList);
		model.addAttribute("questionList", questionList);
		model.addAttribute("tenMostScoreUserList", tenMostScoreUserList);
		model.addAttribute("recommendQuestionList", recommendQuestionList);
		model.addAttribute("categoryList", categoryList);
		// 跳转到指定页面
		return "index";
	}

	// 登录注册页面
	@GetMapping("/authentication/login_register")
	public String login_register() {
		return "login_register";
	}

	// 跳转至编辑个人信息页面
	@GetMapping("/edit")
	public String edit(Model model, Authentication authentication) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User user = userServiceImpl.findByUsername(username);
		model.addAttribute("user", user);
		return "edit";
	}

	// 通过标题关键字进行搜索
	@GetMapping("/search")
	public String search(Model model, String data, Authentication authentication,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "1") Integer page) {
		// 若搜索栏未输入任何数据或只是输入空格，则重定向至主页
		if (data == null || data.trim().equals("")) {
			return "redirect:/";
		}
		// 去除数据左右两侧的空格
		data = data.trim();
		// 获取当前分页及其提问者的数据
		List<Question> questionList = questionServiceImpl.findAllBySearch(data, size, page);
		// 返回相关分页参数
		ArrayList<Integer> pageList = new ArrayList<Integer>();
		Integer count = questionServiceImpl.findTheCountBySearch(data);
		int x = count / size;
		if (count % size != 0) {
			x += 1;
		}
		for (int i = 1; i <= x; i++) {
			pageList.add(i);
		}
		// 将数据放入model
		model.addAttribute("questionList", questionList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("data", data);
		return "index_search";
	}

	// 进入敏感词管理页面
	@GetMapping("/admin_sensitive_word")
	public String admin_sensitive_word(Model model) {
		// 获取所有敏感词
		List<Sensitive_word> sensitive_wordList = sensitive_wordServiceImpl.findAll();
		model.addAttribute("sensitive_wordList", sensitive_wordList);
		return "admin_sensitive_word";
	}

	// 进入用户管理页面
	@GetMapping("/admin_user")
	public String admin_user(Model model) {
		// 获取所有用户
		List<User> userList = userServiceImpl.findAll();
		model.addAttribute("userList", userList);
		return "admin_user";
	}

	// 进入分类管理页面
	@GetMapping("/admin_category")
	public String admin_category(Model model) {
		// 获取所有分类
		List<Category> categoryList = categoryServiceImpl.findAll();
		model.addAttribute("categoryList", categoryList);
		return "admin_category";
	}

	// 搜索时返回相关数据的json格式
	@GetMapping("/dynamicSearch")
	@ResponseBody
	public List<Question> findByDynamicSearch(String data, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "1") Integer page) {
		if (data == null || data.trim().equals("")) {
			return null;
		}
		// 去除数据左右两侧的空格
		data = data.trim();
		// 返回数据（大于10条则只返回10条）
		List<Question> questionList = questionServiceImpl.findAllBySearch(data, size, page);
		if (questionList.size() > 10) {
			ArrayList<Question> arrayList = new ArrayList<Question>();
			for (int i = 0; i < 10; i++) {
				arrayList.add(questionList.get(i));
			}
			return arrayList;
		} else {
			return questionList;
		}
	}

	@GetMapping("/password_change")
	public String password_change() {
		return "password_change";
	}
}
