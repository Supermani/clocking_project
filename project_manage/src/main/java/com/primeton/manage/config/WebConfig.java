package com.primeton.manage.config;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.primeton.manage.employee.interceptor.LoginInterceptor;

@Component
public class WebConfig extends WebMvcConfigurerAdapter {

	public WebConfig() {
		super();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
		registry.addResourceHandler("/templates/**").addResourceLocations(
				ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");

		super.addResourceHandlers(registry);
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//拦截规则：除了login，其他都拦截判断
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/login")
				.excludePathPatterns("/register");
		super.addInterceptors(registry);
	}
}
