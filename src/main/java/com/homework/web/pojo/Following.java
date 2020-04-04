package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//用户与被关注的用户
@Data
@Entity
@Table(name = "following")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Following {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 关注者的id

	@Column(name = "following_id")
	private Integer following_id;// 被关注者的id

}
