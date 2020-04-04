package com.homework.security;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//静态资源映射
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	public String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

	public String staticString = "file:" + path.substring(1, path.indexOf("target/classes/"))
			+ "src/main/resources/static";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 静态资源文件夹的全路径
		registry.addResourceHandler("/image/**")
				.addResourceLocations(staticString + File.separator + "image" + File.separator);
		registry.addResourceHandler("/css/**")
				.addResourceLocations(staticString + File.separator + "css" + File.separator);
		registry.addResourceHandler("/js/**")
				.addResourceLocations(staticString + File.separator + "js" + File.separator);
	}

}
