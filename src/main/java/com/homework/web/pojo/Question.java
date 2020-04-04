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

//提问
@Data
@Entity
@Table(name = "question")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 提问者的id

	@Column(name = "create_time")
	private Date create_time;// 提问的创建时间

	@Column(name = "title")
	private String title;// 提问的标题

	@Column(name = "content")
	private String content;// 提问的内容

	@Column(name = "on_off")
	private Boolean on_off;// 提问是否能回答的开关，true为能回答，false为不能回答

	@Column(name = "answer_id")
	private Integer answer_id;// 置顶回答的id

	@Column(name = "category_id")
	private Integer category_id;// 问题分类的id
	
	@Column(name = "score")
	private Integer score;// 积分
}
