package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//敏感词
@Data
@Entity
@Table(name = "sensitive_word")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Sensitive_word {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "word")
	private String word;// 敏感词
}
