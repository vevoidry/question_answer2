package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.util.JsonObject;
import com.homework.web.pojo.Message;
import com.homework.web.pojo.User;
import com.homework.web.repository.MessageRepository;
import com.homework.web.service.impl.MessageServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;
import com.homework.websocket.WebSocket;

@Controller
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	private MessageServiceImpl messageServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private WebSocket webSocket;

	@GetMapping
	public String message(Authentication authentication, Model model) {
		// 获取登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		ArrayList<Integer> user_idList = new ArrayList<Integer>();
		List<Message> messageList1 = messageServiceImpl.findFiveSend(me.getId());
		List<Message> messageList2 = messageServiceImpl.findFiveReceive(me.getId());
		Iterator<Message> iterator = messageList1.iterator();
		while (iterator.hasNext()) {
			Message next = iterator.next();
//			System.out.println(next.getSend_user_id() + ":" + next.getReceive_user_id() + ":" + next.getContent());
			user_idList.add(next.getReceive_user_id());
		}
		iterator = messageList2.iterator();
		while (iterator.hasNext()) {
			Message next = iterator.next();
//			System.out.println(next.getSend_user_id() + ":" + next.getReceive_user_id() + ":" + next.getContent());
			for (int i = 0; i < user_idList.size(); i++) {
				Integer integer = user_idList.get(i);
				if (integer == next.getSend_user_id()) {
					break;
				} else if (i == user_idList.size() - 1) {
					user_idList.add(next.getSend_user_id());
				}
			}
		}
		ArrayList<User> userList = new ArrayList<User>();
		Iterator<Integer> iterator2 = user_idList.iterator();
		while (iterator2.hasNext()) {
			Integer next = iterator2.next();
			User user = userServiceImpl.findById(next);
			userList.add(user);
		}
		model.addAttribute("userList", userList);
		return "message_html";
	}

	// 跳转至特定用户私信页面
	@GetMapping("/{id:[0-9]*}")
	public String get(@PathVariable Integer id, Model model, Authentication authentication) {
		// 获取登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 获取私信用户信息
		User user = userServiceImpl.findById(id);
		// 获取之前的私信记录
		List<Message> messageList = messageServiceImpl.findAllBySend_user_idReceive_user_idExchange(me.getId(), id);
		// 将私信记录放入model
		System.out.println(messageList.size());
		model.addAttribute("messageList", messageList);
		model.addAttribute("user", user);
		return "message::message_part";
	}

	// 添加一条私信
	@PostMapping("/send")
	@ResponseBody
	public JsonObject postMessage(Message message) {
		if (message.getContent().trim().equals("")) {
			throw new RuntimeException("私信内容不可为空");
		}
		Integer receive_user_id = message.getReceive_user_id();
		Message message2 = messageServiceImpl.save(message);
		// 同时通过websocket向私信方发送数据
		webSocket.sendMessage(message2, receive_user_id);
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("私信发送成功");
		jsonObject.setData("/messages/" + message.getReceive_user_id());
		return jsonObject;
	}

	@GetMapping("/close")
	public String close() {
		return "message::message_close";
	}

}
