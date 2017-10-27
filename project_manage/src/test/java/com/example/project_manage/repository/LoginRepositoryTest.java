package com.example.project_manage.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.primeton.manage.ProjectManageApplication;
import com.primeton.manage.employee.dao.LoginRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManageApplication.class)
public class LoginRepositoryTest {

	
	@Autowired
	LoginRepository loginRepository;
	
	@Test
	public void findByNameTest() {
		String name = "admin";
		String password = "E10ADC3949BA59ABBE56E057F20F883E";
		System.out.println(loginRepository.findByNameAndPassword(name, password));
	}
}
