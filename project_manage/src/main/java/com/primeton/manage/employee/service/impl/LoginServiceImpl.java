package com.primeton.manage.employee.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primeton.manage.employee.dao.LoginRepository;
import com.primeton.manage.employee.entity.Users;
import com.primeton.manage.employee.service.LoginService;
import com.primeton.manage.utils.MD5Util;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginRepository loginRespository;

	@Override
	public Users login(String name, String password) {
		return loginRespository.findByNameAndPassword(name, MD5Util.getMD5(password));
	}

	@Override
	public void register(String name, String password) {
		Users user = new Users();
		user.setName(name);
		user.setPassword(MD5Util.getMD5(password));
		user.setCreateTime(new Date());
		loginRespository.save(user);
	}
	
}
