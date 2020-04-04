package com.homework.web.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//用户
// lombok的注解，自动为所有属性添加setter，getter，hashcode，equals，toString，无参构造器
@Data
//以下注解用来对应数据库中的一个表
@Entity // 说明该类是用来对应数据库中的一个表的
@Table(name = "user") // 说明该类所对应的表的表名
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" }) //
public class User {

	@Id // 设置该属性为主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主键类型
	@Column(name = "id") // 设置该属性对应表中的字段名
	private Integer id;// 主键

	@Column(name = "username")
	private String username;// 用户名，用于登录

	@Column(name = "password")
	private String password;// 密码，用于登录

	@Column(name = "role")
	private String role;// 角色，用于权限控制。普通用户为vip，管理员为admin

	@Column(name = "nickname")
	private String nickname;// 昵称，用于显示

	@Column(name = "image")
	private String image; // 头像

	@Column(name = "gender")
	private String gender; // 性别

	@Column(name = "profile")
	private String profile;// 用户的个人简介

	@Column(name = "score")
	private Integer score;// 积分

	@Column(name = "is_using")
	private Boolean is_using;// 是否可登录，true是可登录，false是不可登录
}
