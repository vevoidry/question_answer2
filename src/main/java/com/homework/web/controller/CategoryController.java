package com.homework.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.util.JsonObject;
import com.homework.web.pojo.Category;
import com.homework.web.pojo.Question;
import com.homework.web.service.impl.CategoryServiceImpl;
import com.homework.web.service.impl.QuestionServiceImpl;

@Controller
@RequestMapping("/categorys")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;

	// 添加一个分类
	@PostMapping
	@ResponseBody
	public JsonObject post(String name) {
		// 取出用户输入数据，去除两侧空格，然后判断数据是否为空
		name = name.trim();
		if (name.equals("")) {
			throw new RuntimeException("分类名不可为空");
		}
		// 判断是否已经存在该分类
		Category category = categoryServiceImpl.findByName(name);
		if (category != null) {
			throw new RuntimeException("分类名已经存在");
		} else {
			// 添加分类
			Category category2 = new Category();
			category2.setName(name);
			categoryServiceImpl.insert(category2);
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("添加成功");
		jsonObject.setData("/admin_category");
		return jsonObject;
	}

	// 修改分类名
	@PutMapping
	@ResponseBody
	public JsonObject put(Category category) {
		// 去除新分类名的两侧空格
		category.setName(category.getName().trim());
		// 判断新分类名是否为空
		if (category.getName().equals("")) {
			throw new RuntimeException("新分类名不可为空");
		}
		// 判断该分类是否已存在
		Category category2 = categoryServiceImpl.findByName(category.getName());
		if (category2 != null) {
			throw new RuntimeException("该分类已存在");
		}
		// 保存新分类
		categoryServiceImpl.update(category);
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("修改成功");
		jsonObject.setData("/admin_category");
		return jsonObject;
	}

	// 根据分类返回相关问题
	@GetMapping("/{id:[0-9]*}")
	public String get(@PathVariable Integer id, @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(defaultValue = "1") Integer page, Model model) {
		// 获取问题数据
		List<Question> questionList = questionServiceImpl.findAllByCategory_idPage(id, size, page);
		// 获取分页数据
		Integer count = questionServiceImpl.findTheCountByCategory_id(id);
		int x = count / size;
		if (count % size != 0) {
			x += 1;
		}
		ArrayList<Integer> pageList = new ArrayList<Integer>();
		for (int i = 1; i <= x; i++) {
			pageList.add(i);
		}
		// 将数据放入model
		model.addAttribute("questionList", questionList);
		model.addAttribute("pageList", pageList);
		model.addAttribute("id", id);
		return "index_category";
	}

	// 删除分类
	@GetMapping("/delete/{id:[0-9]}")
	@Transactional
	public String delete(@PathVariable Integer id) {
		// 把相关问题的分类id都改成0
		questionServiceImpl.updateAllByCategory_id(id);
		// 删除该分类
		categoryServiceImpl.deleteById(id);
		return "redirect:/admin_category";
	}
}
