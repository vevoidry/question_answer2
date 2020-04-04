package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//关注提问
@Data
@Entity
@Table(name = "collection")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Collection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 关注者的id

	@Column(name = "question_id")
	private Integer question_id;// 问题的id

}
