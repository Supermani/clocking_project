package com.primeton.manage.employee.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.primeton.manage.employee.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	@Query(value = "SELECT count(1) FROM holiday WHERE date_type = 0 and date between :startDate and :endDate",nativeQuery = true)
	public Integer getWorkingCount(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

	@Query(value = "SELECT date_type FROM holiday WHERE date=:date" ,nativeQuery = true)
	public Integer findDateTypeBy(@Param("date")Date date);
	
}
