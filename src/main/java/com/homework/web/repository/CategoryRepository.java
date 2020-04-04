package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.web.pojo.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query(value = "select * from category where name=:name", nativeQuery = true)
	Category findByName(String name);
}
