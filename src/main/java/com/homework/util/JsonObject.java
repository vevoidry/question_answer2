package com.homework.util;

import lombok.Data;

@Data
public class JsonObject {

	private Integer code;// 状态码

	private String message;// 信息

	private Object data;// 数据

}
