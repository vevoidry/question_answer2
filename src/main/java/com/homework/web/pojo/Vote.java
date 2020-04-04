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

//对回答的点赞
@Data
@Entity
@Table(name = "vote")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 点赞者的id

	@Column(name = "answer_id")
	private Integer answer_id;// 所属回答的id
}
