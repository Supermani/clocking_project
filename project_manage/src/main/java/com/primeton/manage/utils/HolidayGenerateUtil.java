package com.primeton.manage.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 休息日生成工具类 同时导入数据库（2节假日,1休息日,0工作日）
 * 
 * @author zhangyingwen
 *
 */
public class HolidayGenerateUtil {
	// https://api.goseek.cn/Tools/holiday?date=20180215

	public void holidayGenerate() {

	}

	private List<String> dateRange(String start, String end){
		List<String> list = new ArrayList<>();
		LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyyMMdd"));
		list.add(startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString());
		LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		LocalDate nextDate;
		while(true){
			nextDate = startDate.plus(1, ChronoUnit.DAYS);
			list.add(nextDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString());
			startDate = nextDate;
			
			if(endDate.equals(nextDate))
				break;
		}
		return list;
	}
	
	public static void main(String[] args) {
		HolidayGenerateUtil h = new HolidayGenerateUtil();
		h.dateRange("20180101", "20181231");
	}
	
}
