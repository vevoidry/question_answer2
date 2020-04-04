package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.web.pojo.Share;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.ShareServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/shares")
public class ShareController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ShareServiceImpl shareServiceImpl;

	@GetMapping
	public String getShare() {
		return "share";
	}

	@PostMapping
	public String postShare(Authentication authentication, String content) {
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 若分享内容为空，则直接跳转到用户页面
		if (content == null || content.trim().equals("")) {
			return "redirect:/users/" + me.getId();
		}
		// 保存share
		Share share = new Share();
		share.setContent(content);
		share.setUser_id(me.getId());
		shareServiceImpl.save(share);
		// 跳转到用户页面
		return "redirect:/users/" + me.getId();
	}

	@GetMapping("/delete")
	public String delete(Integer user_id, Integer share_id) {
		shareServiceImpl.deleteById(share_id);
		return "redirect:/users/" + user_id;
	}
}
