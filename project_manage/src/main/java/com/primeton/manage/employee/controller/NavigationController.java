package com.primeton.manage.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
	
	@GetMapping("/index")
	public String navigation() {
		return "navigation";
	}
	
}
