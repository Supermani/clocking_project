package com.primeton.manage.statistics;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.primeton.manage.employee.entity.AttendanceRecord;
import com.primeton.manage.utils.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
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

	/**
	 * 加班费统计
	 * 	规则：超过6点半算加班总小时数，算加班费用10元/小时； 超过8点有餐补 20元/小时
	 * @param date 月份
	 * @return
	 */
	public List<Map<String, Object>> overtimeCostCalc(String date) throws ParseException {
		List<Map<String, Object>> resultList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm");

		String startDate = sdf.format(getFirstDay(sdf.parse(date)));
		String endDate = sdf.format(getLastDay(sdf.parse(date)));

		// TODO 需要调整成批量的，而不是每条查询
		List<EmployeeInfo> empList = empRepository.findAll();
		for (int i = 0; i < empList.size(); i++) {
			EmployeeInfo emp = empList.get(i);
			List<AttendanceRecord> list = attRepository.findByEmpIdAndAttendanceDateBetween(emp.getId(), startDate, endDate);

			double overtimes = 0;
			int mealDay = 0;
			for(AttendanceRecord ar : list){
				String outTime = ar.getSignoutTime();
				if(!StringUtils.isEmpty(outTime)) {
					Date outTimeLong = sd.parse(outTime);
					//加班总小时数
					Date standardEndTimeLong = sd.parse(PropertyUtil.get("standard.endtime"));
					double workOuttime = (int) ((outTimeLong.getTime() - standardEndTimeLong.getTime()) / 1000 / 60); //分钟数
					overtimes += workOuttime;

					//餐补天数	超过8小时
					Date mealTimeLong = sd.parse(PropertyUtil.get("standard.mealtime"));
					if( (outTimeLong.getTime() - mealTimeLong.getTime()) > 0){
						mealDay += 1;
					}
				}
			}
			overtimes = (int) overtimes / 60;
			//加班费用
			double overtimeCost = overtimes * 10;
			//餐补费用
			double mealCost = mealDay * 20;


			Map<String, Object> map = new HashMap<>();
			map.put("Id", emp.id);
			map.put("Name", emp.name);
			map.put("Overtimes", overtimes);
			map.put("OvertimeCost", overtimeCost);
			map.put("MealDay", mealDay);
			map.put("MealCost", mealCost);
			resultList.add(map);
		}

		return resultList;
	}

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

	/**
	 * 获取当月最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	/**
	 * 获取当月第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}


}
