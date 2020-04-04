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

//对提问的回答
@Data
@Entity
@Table(name = "answer")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 回答者的id

	@Column(name = "question_id")
	private Integer question_id;// 所属提问的id

	@Column(name = "create_time")
	private Date create_time;// 回答的创建时间

	@Column(name = "content")
	private String content;// 回答的内容

	@Column(name = "vote_count")
	private Integer vote_count;// 点赞数

}
