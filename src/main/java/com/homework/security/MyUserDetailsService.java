package com.homework.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.homework.web.service.impl.UserServiceImpl;

//获取用户账号，密码，权限用于认证。若认证成功则进入认证成功处理器，否则进入认证失败处理器。
@Component
public class MyUserDetailsService implements UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("用户名为[" + username + "]的用户正在进行身份认证");
		// 获取用户的基本信息
		com.homework.web.pojo.User user = userServiceImpl.findByUsername(username);
		// 获取用户的角色信息
		ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
		// 构建包装了用户信息的对象并返回
		// 判断用户是否被禁用
		if (user.getIs_using()) {
			return new User(username, passwordEncoder.encode(user.getPassword()), authorityList);
		} else {
			return null;
		}
	}

}
