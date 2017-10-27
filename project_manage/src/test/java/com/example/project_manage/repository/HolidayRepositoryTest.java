package com.example.project_manage.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.primeton.manage.ProjectManageApplication;
import com.primeton.manage.employee.dao.AttendanceRepository;
import com.primeton.manage.employee.dao.EmployeeInfoRepository;
import com.primeton.manage.employee.dao.HolidayRepository;
import com.primeton.manage.employee.entity.EmployeeInfo;
import com.primeton.manage.employee.service.impl.HolidayCacheService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManageApplication.class)
public class HolidayRepositoryTest {
	
	@Autowired
	HolidayRepository holidayRepository;
	
	@Autowired
	private AttendanceRepository attRepository;
	
	@Autowired
	private EmployeeInfoRepository empRepository;
	
	@Autowired
	private HolidayCacheService cache;
	
	@Test
	public void getWorkingCount(){
		//System.out.println(holidayRepository.getWorkingCount(Date.valueOf("2017-05-01"), Date.valueOf("2017-05-31")));
		//System.out.println(holidayRepository.findDateTypeBy(Date.valueOf("2017-05-01")));
		//System.out.println(monthlyAttendanceCalc("2017-06-01 00:00:00", "2017-06-30  00:00:00"));
		System.out.println(getWorkingDays(Date.valueOf("2017-06-01 00:00:00"), Date.valueOf("2017-06-30 00:00:00")));
	}
	
	/*
	 * 月出勤率计算
	 */
	public List<Map<String ,Object>> monthlyAttendanceCalc(String startDate, String endDate){
		List<Map<String, Object>> resultList = new ArrayList<>();
		//TODO  需要调整成批量的，而不是每条查询
		List<EmployeeInfo> empList = empRepository.findAll();
		for(int i=0; i<empList.size(); i++){
			EmployeeInfo emp = empList.get(i); 
			int monthlyAttendanceDay = attRepository.findAttendanceDays(emp.id, startDate, endDate); //工作日出勤天数
			int holidayWorkingDays = attRepository.findHolidayWorkingDays(emp.id, startDate, endDate);//节假日加班天数
			double halfDays = attRepository.findHalfDays(emp.id, startDate, endDate)/2.0; //节假日加班半天天数
			double workOverTimeDays = attRepository.findWorkOverTime(emp.id, startDate, endDate)/8.0;//工作日加班天数
			Map<String,Object> map = new HashMap<>();
			map.put("Id", emp.id);
			map.put("Name", emp.name);
			map.put("MonthlyAttendanceDay", monthlyAttendanceDay);
			map.put("HolidayWorkingDays", holidayWorkingDays+halfDays);
			map.put("WorkOverTimeDays", workOverTimeDays);
			int workingDays = getWorkingDays(Date.valueOf(startDate), Date.valueOf(endDate));
			double allDays = monthlyAttendanceDay+holidayWorkingDays+halfDays+workOverTimeDays;
			//人月数=(工作日出勤天数+节假日出勤天数+工作日加班天数)/工作日天数
			double monthlyRadio = new BigDecimal(allDays/workingDays).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			map.put("MonthlyRadio", monthlyRadio);
			resultList.add(map);
		}
		return resultList;
	}
	
	/*
	 * 获取某月工作日天数
	 */
	public Integer getWorkingDays(Date startDate, Date endDate){
		return cache.getWorkingCount(startDate, endDate);
	}
	
	
	public void dateTypeTest() {
		System.out.println(cache.getTypeFromCache("2017-07-15"));
		System.out.println(cache.getTypeFromCache("2017-07-15"));
		System.out.println(cache.getTypeFromCache("2017-07-16"));
		System.out.println(cache.getTypeFromCache("2017-07-15"));
		System.out.println(cache.getTypeFromCache("2017-07-16"));
	}
	
}
