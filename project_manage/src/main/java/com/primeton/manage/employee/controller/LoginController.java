package com.primeton.manage.employee.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.primeton.manage.employee.entity.Users;
import com.primeton.manage.employee.service.LoginService;
import com.primeton.manage.utils.ConstUtils;

@Controller
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;
	
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest request,
			@RequestParam(name="userName") String userName,
			@RequestParam(name="password") String password, Model model) {
		Users user = loginService.login(userName, password);
		if (user != null) {
			request.getSession().setAttribute(ConstUtils.LOGIN_INFO, user);
			return "redirect:/index";
		} else {
			model.addAttribute("errorMsg", "用户名或密码错误");
			return "login/login";
		}
	}
	
	@PostMapping("/register")
	public String register(@RequestParam(name="userName") String userName,
			@RequestParam(name="password") String password, 
			@RequestParam(name="password2") String password2,Model model) {
		if ("".equals(userName) || "".equals(password) || "".equals(password2)) {
			model.addAttribute("errorMsg", "输入不能为空！");
		} else if (!password.equals(password2)) {
			model.addAttribute("errorMsg", "2次密码输入要一致");
		} else {
			loginService.register(userName, password);
			model.addAttribute("msg", "注册成功");
		}
		return "login/login"; 
	}
}
