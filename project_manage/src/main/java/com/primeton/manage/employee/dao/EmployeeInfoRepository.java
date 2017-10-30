package com.primeton.manage.employee.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.primeton.manage.employee.entity.EmployeeInfo;

public interface EmployeeInfoRepository 
	extends PagingAndSortingRepository<EmployeeInfo, Long>, JpaSpecificationExecutor<EmployeeInfo>,JpaRepository<EmployeeInfo, Long>  {

	@Query(value="select id from employee_info", nativeQuery=true)
	List<BigInteger> findAllEmpId();
	
	@Query(value="select ar.id from employee_info ei,attendance_record ar where ei.id=ar.emp_id and ar.emp_id=?1", nativeQuery=true)
	List<BigInteger> findIds(String id);
}
