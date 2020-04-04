package com.homework.security;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// 授权管理，permitAll()为不需要任何权限，即游客即可访问。.authenticated()则为必须登录才可访问。
				.authorizeRequests()
				// antMatchers和regexMatchers是两种不同的字符串匹配方法，可搭配使用
//				.regexMatchers(HttpMethod.GET, "/").permitAll()// 获取主页
//				.regexMatchers(HttpMethod.GET, "/authentication/login_register").permitAll()// 跳转至登录注册页面
				.regexMatchers(HttpMethod.GET, "/edit").authenticated()// 跳转至编辑个人信息页面
//				.regexMatchers(HttpMethod.GET, "/search.*").permitAll()// 通过标题关键字进行搜索
				.regexMatchers(HttpMethod.GET, "/admin_sensitive_word").hasAuthority("ROLE_admin")// 进入敏感词管理页面
				.regexMatchers(HttpMethod.GET, "/admin_user").hasAuthority("ROLE_admin")// 进入用户管理页面
				.regexMatchers(HttpMethod.GET, "/admin_category").hasAuthority("ROLE_admin")// 进入分类管理页面
//				.regexMatchers(HttpMethod.GET, "/dynamicSearch").permitAll()//动态搜索时会实时显示相关数据
//				.regexMatchers(HttpMethod.GET, "/password_change").permitAll()//跳转至修改密码页面

				.regexMatchers(HttpMethod.GET, "/answers/[0-9]*").authenticated()// 跳转至回答页面
				.regexMatchers(HttpMethod.POST, "/answers").authenticated()// 进行回答
				.regexMatchers(HttpMethod.GET, "/answers/delete.*").authenticated()// 删除回答

				.regexMatchers(HttpMethod.POST, "/categorys").hasAuthority("ROLE_admin")// 添加一个分类
				.regexMatchers(HttpMethod.PUT, "/categorys").hasAuthority("ROLE_admin")// 修改一个分类
				.regexMatchers(HttpMethod.GET, "/categorys/[0-9]*.*").permitAll()// 返回分类的问题页面
				.regexMatchers(HttpMethod.DELETE, "/categorys/delete/[0-9]*").hasAuthority("ROLE_admin")// 删除一个分类

				.regexMatchers(HttpMethod.GET, "/collections.*").authenticated()// 对某个问题进行关注或取消关注

				.regexMatchers(HttpMethod.GET, "/followings.*").authenticated()// 对某个用户进行关注或取消关注

				.regexMatchers(HttpMethod.GET, "/messages/[0-9]*").authenticated()// 跳转至特定用户私信页面
				.regexMatchers(HttpMethod.POST, "/messages/send").authenticated()// 添加一条私信

				.regexMatchers(HttpMethod.GET, "/questions").authenticated()// 跳转至提问页面
				.regexMatchers(HttpMethod.POST, "/questions").authenticated()// 进行提问
				.regexMatchers(HttpMethod.GET, "/questions/[0-9]*").permitAll()// 跳转至某个问题页面
				.regexMatchers(HttpMethod.GET, "/questions/[0-9]*/close").authenticated()// 开启或关闭某个问题的回答功能
				.regexMatchers(HttpMethod.GET, "/questions/[0-9]*/top/[0-9]*").authenticated()// 对某个回答进行置顶
				.regexMatchers(HttpMethod.GET, "/questions/delete.*").authenticated()// 删除提问

				.regexMatchers(HttpMethod.POST, "/sensitive_words").hasAuthority("ROLE_admin")// 添加敏感词
				.regexMatchers(HttpMethod.GET, "/sensitive_words/[0-9]*").hasAuthority("ROLE_admin")// 移除相应敏感词

				.regexMatchers(HttpMethod.GET, "/shares").authenticated()// 跳转至动态分享页面
				.regexMatchers(HttpMethod.POST, "/shares").authenticated()// 进行动态分享
				.regexMatchers(HttpMethod.GET, "/shares/delete").authenticated()// 删除某条动态

				.regexMatchers(HttpMethod.POST, "/users").permitAll()// 进行注册
				.regexMatchers(HttpMethod.GET, "/users/[0-9]*").permitAll()// 跳转至某个用户的个人主页
				.regexMatchers(HttpMethod.PUT, "/users/[0-9]*").authenticated()// 修改除头像外的个人信息
				.regexMatchers(HttpMethod.PUT, "/users/[0-9]*/image").authenticated()// 修改头像
				.regexMatchers(HttpMethod.GET, "/users/edit/[0-9]*").hasAuthority("ROLE_admin")// 使用户可登录或禁止登录
				.regexMatchers(HttpMethod.POST, "/users/password_change").permitAll()// 修改密码
				
				.regexMatchers(HttpMethod.GET, "/votes.*").authenticated()// 进行点赞或取消点赞

				.anyRequest().permitAll()// 剩下的url一律不需要任何权限

				// 认证管理
				.and().formLogin().loginPage("/authentication/login_register")
				.loginProcessingUrl("/authentication/authenticate").successHandler(myAuthenticationSuccessHandler)
				.failureHandler(myAuthenticationFailureHandler)

				// 退出管理
				.and().logout().logoutUrl("/authentication/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				.and().csrf().disable();
	}

	// 用于注册后自动登录
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
