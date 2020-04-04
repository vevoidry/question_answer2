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

//私信
@Data
@Entity
@Table(name = "message")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键

	@Column(name = "send_user_id")
	private Integer send_user_id;// 发送者的id

	@Column(name = "receive_user_id")
	private Integer receive_user_id;// 接收者的id

	@Column(name = "create_time")
	private Date create_time;// 私信的创建时间

	@Column(name = "content")
	private String content;// 私信的内容

	@Column(name = "is_read")
	private Boolean is_read;// 是否读过
}
