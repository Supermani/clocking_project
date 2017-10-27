package com.primeton.manage.employee.service;

import com.primeton.manage.employee.entity.Users;

public interface LoginService {

	Users login(String name, String password);
	
	void register(String name, String password);
}
