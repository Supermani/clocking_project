package com.primeton.manage.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.primeton.manage.employee.entity.EmployeeInfo;

public interface EmployeeInfoService {

	public List<EmployeeInfo> getAll();

	public Page<EmployeeInfo> getEmpsByPage(int pageNumber, int pageSize,
			String name, String startDate, String endDate, String isOnJob);
	
	public EmployeeInfo getOne(Long id);
	
	public void delete(String id);
	
	void add(String empNumber, String empName);
	
	void edit(String id, String name, String startDate, String endDate, String isOnJob);
}
