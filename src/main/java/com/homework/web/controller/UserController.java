package com.homework.web.controller;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.homework.security.MyUserDetailsService;
import com.homework.util.JsonObject;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.Following;
import com.homework.web.pojo.Question;
import com.homework.web.pojo.Share;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.CollectionServiceImpl;
import com.homework.web.service.impl.FollowingServiceImpl;
import com.homework.web.service.impl.QuestionServiceImpl;
import com.homework.web.service.impl.ShareServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private FollowingServiceImpl followingServiceImpl;
	@Autowired
	private CollectionServiceImpl collectionServiceImpl;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	@Autowired
	private ShareServiceImpl shareServiceImpl;

	// 注册新用户
	@PostMapping
	@ResponseBody
	public JsonObject postUsers(User user, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, Model model) {
		// 获取用户名与密码并去除两侧空格
		String username = user.getUsername().trim();
		String password = user.getPassword().trim();
		// 判断位数是否符合要求
		if (username.length() < 6 || username.length() > 15 || password.length() < 6 || password.length() > 15) {
			throw new RuntimeException("用户名与密码的长度必须为6-15位");
		}
		// 判断输入的数据是否符合要求（必须为大小写字母或数字）
		Pattern p = Pattern.compile("[0-9A-Za-z]*");
		Matcher m1 = p.matcher(username);
		Matcher m2 = p.matcher(password);
		if (!m1.matches() || !m2.matches()) {
			throw new RuntimeException("用户名与密码只能为数字或大小写字母");
		}
		// 判断用户名是否已经被使用
		User user2 = userServiceImpl.findByUsername(username);
		if (user2 != null) {
			throw new RuntimeException("该用户名已被使用，请重新输入");
		}
		// 根据注册信息创建新用户
		User user3 = userServiceImpl.save(user);
		// 以下为实现自动登录功能
		// 生成Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user3.getUsername(),
				user3.getPassword());
		try {
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authenticatedUser = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
			// 设置session的user数据
			request.getSession().setAttribute("user", user3);
		} catch (Exception e) {
			System.out.println("Authentication failed: " + e.getMessage());
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("注册成功");
		jsonObject.setData("/");
		return jsonObject;
	}

	// 跳转至某个用户的个人主页
	@GetMapping("/{id:[0-9]*}")
	public String getUsersById(@PathVariable Integer id, Model model, Authentication authentication) {
		// 判断用户是否已登录
		if (authentication != null) {
			String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
					.getUsername();
			User me = userServiceImpl.findByUsername(username);
			// 若用户已登录，则判断用户所查看的页面是否为登陆者自身的主页
			if (me.getId().equals(id)) {
				model.addAttribute("isMe", true);
				// 若用户查看的是自己的主页，则再次判断该用户是否为管理员
				if (me.getRole().equals("admin")) {
					model.addAttribute("isAdmin", true);
				}
			} else {
				// 若不是本人，则判断是否已经关注了该用户
				model.addAttribute("isNotMe", true);
				Following following = followingServiceImpl.findByUser_idFollowing_id(me.getId(), id);
				if (following == null) {
					model.addAttribute("notFollowing", true);
				} else {
					model.addAttribute("notFollowing", false);
				}
			}
		} else {
			// 若用户未登录
			model.addAttribute("isNotMe", true);// 不是本人设置为true
			model.addAttribute("notFollowing", true);// 未关注该用户设置为true
		}
		// 获取用户信息
		User user = userServiceImpl.findById(id);
		user.setPassword(null);// 将用户密码设置为null
		// 获取用户的关注者数据
		ArrayList<User> followingUserList = new ArrayList<User>();
		List<Following> followingList = followingServiceImpl.findAllByUser_id(user.getId());
		Iterator<Following> followingListIterator = followingList.iterator();
		while (followingListIterator.hasNext()) {
			followingUserList.add(userServiceImpl.findById(followingListIterator.next().getFollowing_id()));
		}
		// 获取用户的粉丝数据
		ArrayList<User> followerUserList = new ArrayList<User>();
		List<Following> followerList = followingServiceImpl.findAllByFollowing_id(user.getId());
		Iterator<Following> followerListIterator = followerList.iterator();
		while (followerListIterator.hasNext()) {
			followerUserList.add(userServiceImpl.findById(followerListIterator.next().getUser_id()));
		}
		// 获取用户的关注问题的数据
		ArrayList<Question> questionList = new ArrayList<Question>();
		List<Collection> collectionList = collectionServiceImpl.findAllByUser_id(id);
		Iterator<Collection> collectionListIterator = collectionList.iterator();
		while (collectionListIterator.hasNext()) {
			questionList.add(questionServiceImpl.findById(collectionListIterator.next().getQuestion_id()));
		}
		// 获取用户发表过的问题
		List<Question> questionOfUserList = questionServiceImpl.findAllByUser_id(id);
		// 获取用户的动态分享
		List<Share> shareList = shareServiceImpl.findAllByUser_id(user.getId());
		// 将数据放入model
		model.addAttribute("user", user);// 用户个人数据
		model.addAttribute("followingUserList", followingUserList);// 关注用户的数据
		model.addAttribute("followerUserList", followerUserList);// 粉丝的数据
		model.addAttribute("questionList", questionList);// 关注问题的数据
		model.addAttribute("questionOfUserList", questionOfUserList);// 用户发表过的问题数据
		model.addAttribute("shareList", shareList);// 用户的动态分享
		// 跳转至用户个人主页
		return "user";
	}

	// 修改除头像外的个人数据
	@PutMapping("/{id:[0-9]*}")
	public String putUserById(User user, Authentication authentication, HttpServletRequest request) {
		// 获取已登录用户的信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 根据传入的数据更新用户的性别，昵称，简介并保存
		me.setGender(user.getGender());
		me.setNickname(user.getNickname());
		me.setProfile(user.getProfile());
		User user2 = userServiceImpl.update(me);
		// 更新session里的user
		request.getSession().setAttribute("user", user2);
		// 跳转至个人主页
		return "redirect:/users/" + user2.getId();
	}

	// 修改头像
	@PostMapping("/{userId:[0-9]*}/image")
	public String postUserImage(Authentication authentication, MultipartFile filename, @PathVariable Integer userId,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		String name = filename.getOriginalFilename();
//		System.out.println(name);
		String x = name.substring(name.lastIndexOf("."));
//		System.out.println(x);
		// 判断传输的图片名是否为空
		if (filename.getOriginalFilename().equals("")) {// 不能用==null进行判断
			throw new RuntimeException("您上传的头像有问题");
		}
		// 获取已登录的用户信息
		String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal())
				.getUsername();
		User me = userServiceImpl.findByUsername(username);
		// 保存到static下的image文件夹中
		String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/image/";
		// 通过UUID获取随机字符串并为图片重命名，防止图片名冲突
		String imageName = UUID.randomUUID().toString().replaceAll("-", "") + x;
		File file = new File(directoryPath, imageName);
		if (!file.exists()) {
			file.createNewFile();
		}
		filename.transferTo(file);
		// 将图片设置为头像并保存
		me.setImage(imageName);
		User user2 = userServiceImpl.update(me);
		// 更新session里的user
		request.getSession().setAttribute("user", user2);
		// 跳转至个人主页
		return "redirect:/users/" + me.getId();
	}

	// 传入用户id，使用户禁止或可以登录
	@GetMapping("/edit/{id:[0-9]*}")
	public String edit(@PathVariable Integer id) {
		User user = userServiceImpl.findById(id);
		user.setIs_using(!user.getIs_using());
		userServiceImpl.update(user);
		return "redirect:/admin_user";
	}

	// 修改密码
	@PostMapping("/password_change")
	@ResponseBody
	public JsonObject changePassword(String username, String password, String new_password1, String new_password2) {
		User user = userServiceImpl.findByUsername(username);
		if (user == null) {
			throw new RuntimeException("该账号不存在");
		}
		if (!user.getPassword().equals(password)) {
			throw new RuntimeException("旧密码不正确，请重新输入");
		}
		if (!new_password1.equals(new_password2)) {
			throw new RuntimeException("两次新密码不一致，请重新操作");
		}
		if (user.getPassword().equals(new_password1)) {
			throw new RuntimeException("新密码不可与旧密码相同，请重新操作");
		}
		if (new_password1.length() < 6 || new_password1.length() > 15) {
			throw new RuntimeException("新密码长度必须为6-15");
		}
		Pattern p = Pattern.compile("[0-9A-Za-z]*");
		if (!(p.matcher(new_password1).matches())) {
			throw new RuntimeException("新密码只能为数字或大小写字母");
		}
		// 修改密码
		user.setPassword(new_password1);
		userServiceImpl.update(user);
		JsonObject jsonObject = new JsonObject();
		jsonObject.setCode(200);
		jsonObject.setMessage("修改密码成功");
		jsonObject.setData("/authentication/login_register");
		return jsonObject;
	}

}
