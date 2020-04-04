package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Collection;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.CollectionServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/collections")
public class CollectionController {
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private CollectionServiceImpl collectionServiceImpl;

	@GetMapping
	public String collection(Integer question_id, Authentication authentication) {
		// 判断用户是否已登录
		if (authentication != null) {
			// 获取已登录用户信息
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			// 判断用户是否已关注该问题，若未关注，则效果为关注该问题，否则效果为取消关注
			Collection collection = collectionServiceImpl.findByUser_idQuestion_id(me.getId(), question_id);
			if (collection != null) {
				collectionServiceImpl.deleteById(collection.getId());
			} else {
				Collection collection2 = new Collection();
				collection2.setQuestion_id(question_id);
				collection2.setUser_id(me.getId());
				collectionServiceImpl.save(collection2);
			}
		}
		// 跳转至问题页面
		return "redirect:/questions/" + question_id;
	}

}
