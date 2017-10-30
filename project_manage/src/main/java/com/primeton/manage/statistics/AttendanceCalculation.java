package com.primeton.manage.statistics;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.primeton.manage.employee.dao.AttendanceRepository;
import com.primeton.manage.employee.dao.EmployeeInfoRepository;
import com.primeton.manage.employee.entity.EmployeeInfo;
import com.primeton.manage.employee.service.impl.HolidayCacheService;

@Component
public class AttendanceCalculation {

	@Autowired
	private HolidayCacheService holidayCacheService;

	@Autowired
	private AttendanceRepository attRepository;

	@Autowired
	private EmployeeInfoRepository empRepository;

	/*
	 * 月出勤率计算
	 */
	public List<Map<String, Object>> monthlyAttendanceCalc(String startDate, String endDate) throws ParseException {
		List<Map<String, Object>> resultList = new ArrayList<>();
		// TODO 需要调整成批量的，而不是每条查询
		List<EmployeeInfo> empList = empRepository.findAll();
		for (int i = 0; i < empList.size(); i++) {
			EmployeeInfo emp = empList.get(i);
			int monthlyAttendanceDay = attRepository.findAttendanceDays(emp.id, startDate, endDate); // 工作日出勤天数
			double workOverTimes = attRepository.findWorkOverTime(emp.id, startDate, endDate);// 工作日加班小时数
			double workOverTimeDays = workOverTimes / 8.0;// 工作日加班天数
			/*
			 * 节假日加班天数=半天天数 + 一天天数 + 小时数
			 * 	加班小时数 < 4h == 0.5day 半天
			 * 	4h<= 加班小时数 <= 8h   == 1day 一天
			 *  加班小时数 > 8h  == 1 + (加班小时数-8)/8 day 一天+多出来的小时数
			 */
			int holidayWorkingDays = attRepository.findHolidayWorkingDays(emp.id, startDate, endDate);// 节假日加班天数(4-8h)
			double halfDays = attRepository.findHalfDays(emp.id, startDate, endDate) / 2.0; // 节假日加班半天天数(<4h)
			double holidayWorkingHourDays = attRepository.findHolidayWorkingHours(emp.id, startDate, endDate);// 节假日加班小时数（转换天）(>8h)
			double allHolidayWorkings = holidayWorkingDays + halfDays + holidayWorkingHourDays / 8.0; // 节假日加班总天数

			Map<String, Object> map = new HashMap<>();
			map.put("Id", emp.id);
			map.put("Name", emp.name);
			map.put("MonthlyAttendanceDay", monthlyAttendanceDay);
			map.put("HolidayWorkingDays", allHolidayWorkings);
			map.put("WorkOverTimes", workOverTimes);
			map.put("WorkOverTimeDays", workOverTimeDays);
			int workingDays = getWorkingDays(startDate, endDate);
			double allDays = monthlyAttendanceDay + allHolidayWorkings + workOverTimeDays;
			// 人月数=(工作日出勤天数+节假日出勤天数+工作日加班天数)/工作日天数
			double monthlyRadio = 0.0;
			if (workingDays != 0) {
				monthlyRadio = new BigDecimal(allDays / workingDays).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}

			map.put("MonthlyRadio", monthlyRadio);
			resultList.add(map);
		}
		return resultList;
	}

	/*
	 * 获取某月工作日天数
	 */
	public Integer getWorkingDays(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		return holidayCacheService.getWorkingCount(sdf.parse(startDate), sdf.parse(endDate));
	}

}
