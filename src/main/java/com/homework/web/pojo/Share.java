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

//动态分享
@Data
@Entity
@Table(name = "share")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Share {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "user_id")
	private Integer user_id;// 用户的id

	@Column(name = "create_time")
	private Date create_time;// 动态的创建时间

	@Column(name = "content")
	private String content;// 动态的内容

}
