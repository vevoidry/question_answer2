package com.homework.web.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//问题分类
@Data
@Entity
@Table(name = "category")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "name")
	private String name;// 分类名
}
