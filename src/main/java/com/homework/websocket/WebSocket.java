package com.homework.websocket;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//WebSocket类，用于完成私信
@Component
//WebSocket访问地址
@ServerEndpoint(value = "/webSocket/{user_id}", encoders = { MessageServerEncoder.class })
@Slf4j
public class WebSocket {
	// 定义Websocket容器，储存session
	private static Map<Integer, Session> clients = new ConcurrentHashMap<Integer, Session>();

	// 对应前端的一些事件
	// 建立连接
	@OnOpen
	public void opOpen(Session session, @PathParam(value = "user_id") Integer user_id) {
//		System.out.println("服务器建立连接");
//		System.out.println(user_id);
		clients.put(user_id, session);
	}

	// 关闭连接
	@OnClose
	public void onClose(@PathParam(value = "user_id") Integer user_id) {
//		System.out.println("服务器关闭连接");
		clients.remove(user_id);
	}

	// 接收消息
	@OnMessage
	public void onMessage(String message) {
//		System.out.println("服务器接收消息：" + message);
	}

	// 发送消息
	public void sendMessage(Object message, Integer receive_user_id) {
//		System.out.println("服务器发送消息：" + message);
		Session session = clients.get(receive_user_id);
		if (session != null) {
			try {
				session.getBasicRemote().sendObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}
	}
}
