package com.primeton.manage.employee.dao;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.primeton.manage.employee.entity.AttendanceRecord;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
	
	@Query(value = "SELECT COALESCE(sum(work_outtime),0) FROM attendance_record WHERE emp_id=:empId and attendance_date between :startDate and :endDate AND flag!=5",nativeQuery = true)
	public Double findWorkOverTime(@Param("empId") Long empId, @Param("startDate") String startDate,@Param("endDate") String endDate);

	@Query(value = "SELECT COALESCE(sum(work_outtime),0) FROM attendance_record WHERE emp_id=:empId and attendance_date between :startDate and :endDate AND flag=5 AND work_outtime > 8",nativeQuery = true)
	public Double findHolidayWorkingHours(@Param("empId") Long empId, @Param("startDate") String startDate,@Param("endDate") String endDate);

	@Query(value = "SELECT count(1) FROM attendance_record WHERE emp_id=:empId and attendance_date between :startDate and :endDate AND flag=5 AND work_outtime BETWEEN 4 AND 8",nativeQuery = true)
	public Integer findHolidayWorkingDays(@Param("empId") Long empId, @Param("startDate") String startDate,@Param("endDate") String endDate);
	
	@Query(value = "SELECT count(1) FROM attendance_record WHERE emp_id=:empId and attendance_date between :startDate and :endDate AND flag=6",nativeQuery = true)
	public Integer findHalfDays(@Param("empId") Long empId, @Param("startDate") String startDate,@Param("endDate") String endDate);
	
	@Query(value = "SELECT count(1) FROM attendance_record WHERE emp_id=:empId and attendance_date between :startDate and :endDate AND flag in (0,1,2,3,4)",nativeQuery = true)
	public Integer findAttendanceDays(@Param("empId") Long empId, @Param("startDate") String startDate,@Param("endDate") String endDate);
	
	@Query(value = "SELECT count(1) FROM attendance_record WHERE emp_id=:empId and attendance_date=:date",nativeQuery = true)
	public Integer existRecord(@Param("date") Date date, @Param("empId") Long empId);
}
