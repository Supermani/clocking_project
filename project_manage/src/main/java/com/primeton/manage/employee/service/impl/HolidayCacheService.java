package com.primeton.manage.employee.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.primeton.manage.employee.dao.HolidayRepository;

@Component
public class HolidayCacheService {

	private Map<String, Integer> holidayCache = new HashMap<>();

	@Autowired
	private HolidayRepository holidayRepository;

	/** 
	 * 查询 
	 * 如果数据没有缓存,那么从dataMap里面获取,如果缓存了, 
	 * 那么从guavaDemo里面获取 
	 * 并且将缓存的数据存入到 guavaCacheHoliday里面 
	 * 其中key 为 #date
	 */
	@Cacheable(value = "guavaCacheHoliday", key = "#date")
	public Integer getTypeFromCache(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " : query data is " + date);

		Integer dateType = holidayCache.get(date);

		// 缓存没有
		if (dateType == null) {
			System.out.println("缓存没有命中, 查询数据库");
			dateType = holidayRepository.findDateTypeBy(java.sql.Date.valueOf(date));
			return updateCache(date, dateType);
		}

		return holidayCache.get(date);
	}

	@CachePut(value = "guavaCacheHoliday", key = "#date")
	public Integer updateCache(String date, Integer dateType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " : add data ,date is " + date);
		holidayCache.put(date, dateType);
		// data persistence  
		return dateType;
	}
	
	/*
	 * 获取某月工作日天数
	 */
	public Integer getWorkingCount(Date startDate, Date endDate){
		return holidayRepository.getWorkingCount(startDate, endDate);
	}
	
	
}
