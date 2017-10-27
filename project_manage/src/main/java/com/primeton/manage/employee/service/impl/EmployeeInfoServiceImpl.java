package com.primeton.manage.employee.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.primeton.manage.employee.dao.AttendanceRepository;
import com.primeton.manage.employee.dao.EmployeeInfoRepository;
import com.primeton.manage.employee.entity.EmployeeInfo;
import com.primeton.manage.employee.service.EmployeeInfoService;
import com.primeton.manage.employee.service.PageQuery;

@Service("employeeInfoService")
public class EmployeeInfoServiceImpl implements EmployeeInfoService {

	@Autowired
	private EmployeeInfoRepository repository;
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<EmployeeInfo> getAll() {
		return repository.findAll();
	}


	@Override
	public Page<EmployeeInfo> getEmpsByPage(int pageNumber, int pageSize, 
			String name, String startDate, String endDate, String isOnJob) {
		//获取list
		List<EmployeeInfo> list = findByQuery(name, startDate, endDate, isOnJob, entityManager);
		//获取分页信息
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPage(pageNumber);
		pageQuery.setSize(pageSize);
		Pageable pageable = DataHelper.toPageable(pageQuery);
		//分页显示
		List<EmployeeInfo> anyList = new ArrayList<EmployeeInfo>();
		int max = pageQuery.getPage() * pageQuery.getSize();
		if (max >= list.size()) {
			max = list.size();
		}
		for (int i = (pageQuery.getPage() - 1) * pageQuery.getSize(); i < max; i++) {
			anyList.add(list.get(i));
		}
		Page<EmployeeInfo> sourceCodes = new org.springframework.data.domain.PageImpl<>(anyList, pageable, list.size());
		return sourceCodes;
	}

	@Override
	public EmployeeInfo getOne(Long id) {
		return repository.findOne(id);
	}


	@Override
	public void delete(String id) {
		List<BigInteger> ids = repository.findIds(id); 
		for (BigInteger recordId : ids) {
			attendanceRepository.delete(recordId.longValue());
		}
		repository.delete(Long.parseLong(id));
	}


	@Override
	public void add(String empNumber, String empName) {
		EmployeeInfo ei = new EmployeeInfo();
		ei.setId(Long.parseLong(empNumber));
		ei.setName(empName);
		ei.setIsOnJob(1);
		repository.save(ei);
		
	}

	@Override
	public void edit(String id, String name, String startDate, String endDate, String isOnJob) {
		EmployeeInfo ei = new EmployeeInfo();
		ei.setId(Long.parseLong(id));
		ei.setName(name);
		ei.setInductionTime(Timestamp.valueOf(startDate));
		if (endDate != null && !"".equals(endDate)) {
			ei.setResignationTime(Timestamp.valueOf(endDate));
		}
		ei.setIsOnJob(Integer.parseInt(isOnJob));
		repository.save(ei);
	}
	
	@SuppressWarnings("unchecked")
	private List<EmployeeInfo> findByQuery(String name, String startDate, String endDate, String isOnJob, EntityManager entityManager) {
		StringBuilder stb = new StringBuilder();
		stb.append("select new com.primeton.manage.employee.entity.EmployeeInfo(ei.id as id, ei.name as name, ei.inductionTime as inductionTime,"
				+ " ei.resignationTime as resignationTime, ei.isOnJob as isOnJob "
				+ ") from EmployeeInfo ei where 1=1 ");
		if (!"".equals(name) && name != null && !"null".equals(name)) {
			stb.append(" and ei.name like '%").append(name).append("%' ");
		} 
		if (!"".equals(startDate) && startDate != null && !"null".equals(startDate)) {
			stb.append(" and ei.inductionTime >'").append(startDate).append("'");
		} 
		if (!"".equals(endDate) && endDate != null && !"null".equals(endDate)) {
			stb.append(" and ei.resignationTime <'").append(endDate).append("'");
		} 
		if (!"".equals(isOnJob) && isOnJob != null && !"null".equals(isOnJob)) {
			stb.append(" and ei.isOnJob = ").append(isOnJob);
		} 
		
		return entityManager.createQuery(stb.toString()).getResultList();
	}
}
