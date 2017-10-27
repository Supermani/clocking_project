package com.primeton.manage.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.primeton.manage.employee.entity.AttendanceRecord;


public interface AttendanceRecordService {

	public void importRecord(MultipartHttpServletRequest multiReq);
	
	public List<AttendanceRecord> getAll();

	public Page<AttendanceRecord> getAttendancesByPage(int pageNumber, int pageSize, String name, String startDate, String endDate, String isOnJob); 
	
	public AttendanceRecord modify(AttendanceRecord atten);
}
