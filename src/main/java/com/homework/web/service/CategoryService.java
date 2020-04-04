package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Category;

public interface CategoryService {
	List<Category> findAll();

	Category findByName(String name);

	Category findById(Integer id);

	Category insert(Category category);

	Category update(Category category);

	void deleteById(Integer id);
}
