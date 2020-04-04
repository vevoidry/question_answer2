package com.homework.websocket;

import java.util.Date;

import javax.websocket.EncodeException;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;

import com.homework.web.pojo.Message;

//Message的转换工具类，用于将message里的数据转换成json，因为js处理json会很简单
public class MessageServerEncoder implements Text<Message> {

	@Override
	public void init(EndpointConfig endpointConfig) {
	}

	@Override
	public void destroy() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public String encode(Message message) throws EncodeException {
		Date create_time = message.getCreate_time();
		int year = create_time.getYear() + 1900;
		int month = create_time.getMonth();
		int date = create_time.getDate();
		int hours = create_time.getHours();
		int minutes = create_time.getMinutes();
		int seconds = create_time.getSeconds();
		String time = year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
		return "{\"content\":\"" + message.getContent() + "\",\"create_time\":\"" + time + "\"}";
	}

}
