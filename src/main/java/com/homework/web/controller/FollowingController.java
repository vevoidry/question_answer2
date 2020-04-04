package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Following;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.FollowingServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/followings")
public class FollowingController {
	@Autowired
	private FollowingServiceImpl followingServiceImpl;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping
	public String getFollowings(Integer following_id, Authentication authentication) {
		// 获取已登录用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 判断用户是否已关注该用户，若未关注该用户，则效果为进行关注，否则效果为取消关注
		Following following = followingServiceImpl.findByUser_idFollowing_id(me.getId(), following_id);
		if (following == null) {
			Following following2 = new Following();
			following2.setUser_id(me.getId());
			following2.setFollowing_id(following_id);
			followingServiceImpl.save(following2);
		} else {
			followingServiceImpl.deleteById(following.getId());
		}
		// 跳转至用户页面
		return "redirect:/users/" + following_id;
	}
}
