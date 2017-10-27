package com.primeton.manage.employee.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.primeton.manage.employee.dao.AttendanceRepository;
import com.primeton.manage.employee.entity.AttendanceRecord;
import com.primeton.manage.employee.service.AttendanceRecordService;
import com.primeton.manage.employee.service.PageQuery;
import com.primeton.manage.utils.AttendanceStatus;
import com.primeton.manage.utils.ExcelUtils;
import com.primeton.manage.utils.PropertyUtil;

@Service("attendanceRecordService")
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

	private static Logger logger = LoggerFactory.getLogger(AttendanceRecordServiceImpl.class);

	@Autowired
	private AttendanceRepository repository;

	@Autowired
	private HolidayCacheService holidayCacheService;

	@PersistenceContext
	EntityManager entityManager;

	public void importRecord(MultipartHttpServletRequest multiReq) {
		try (InputStream fis = multiReq.getFile("file1").getInputStream()) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			
			Workbook wb = null;

			MultipartFile file = multiReq.getFile("file1");
			// 获取上传文件的路径
			String uploadFilePath = file.getOriginalFilename();

			if (uploadFilePath != null) {
				boolean isE2007 = false; //判断是否是excel2007及其以上版本
				String fileName = file.getOriginalFilename();
				if (fileName.endsWith("xlsx")) {
					isE2007 = true;
				}

				//根据文件格式(2003或者2007)来初始化
				if (isE2007) {
					wb = new XSSFWorkbook(file.getInputStream());
				} else {
					wb = new HSSFWorkbook(file.getInputStream());
				}

				Sheet sheet = wb.getSheetAt(0);

				int startRowNum = 1;
				int lastRowNum = sheet.getLastRowNum();
				List<AttendanceRecord> list = new ArrayList<AttendanceRecord>(lastRowNum + 1);
				while (startRowNum <= lastRowNum) {
					Row row = sheet.getRow(startRowNum);
					if(row == null){
						logger.info("检测当前id列为空, 可能为异常数据跳过该行. 行号:" + startRowNum);
						startRowNum += 1;
						continue;
					}
					String idStr = ExcelUtils.parseExcel(row.getCell(0));
					if (StringUtils.isEmpty(idStr)) {
						logger.info("检测当前id列为空, 可能为异常数据跳过该行. 行号:" + row.getRowNum());
						startRowNum += 1;
						continue;
					}
					
					Long id = Long.valueOf(idStr);

					String week = row.getCell(2).getStringCellValue();

					java.sql.Date attendanceDate = java.sql.Date.valueOf(ExcelUtils.parseExcel(row.getCell(3)));

					String inTime = ExcelUtils.parseExcel(row.getCell(6));

					String outTime = ExcelUtils.parseExcel(row.getCell(7));
					
					if(StringUtils.isEmpty(inTime) && StringUtils.isEmpty(outTime)){
						logger.info("签到和签退时间都为空, 可能为无效数据跳过该行. 行号:" + startRowNum);
						startRowNum += 1;
						continue;
					}
					
					//检测该月份数据是否导入过，如果导入过将结束，不重复导入。
					int existRecord = repository.existRecord(attendanceDate);
					if(existRecord > 0){
						logger.info("导入记录已存在，无法导入. 行号:" + startRowNum);
						break;
					}
					
					//计算
					Map<String, Object> map = attendInfoCalc(inTime, outTime, attendanceDate);
					String lateTime = (String) map.get("LateTime");
					String leaveTime = (String) map.get("LeaveTime");
					double workOuttime = (double) map.get("WorkOuttime");
					int flag = (int) map.get("Flag");

					list.add(new AttendanceRecord(id, attendanceDate, week, inTime, outTime, lateTime, leaveTime,
							workOuttime, flag));
					startRowNum += 1;
				}
				repository.save(list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算 迟到、早退、加班
	 * @param inTime 签到时间
	 * @param outTime 签退时间
	 * @param attendanceDate 签到日期
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> attendInfoCalc(String inTime, String outTime, Date attendanceDate) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		int flag = AttendanceStatus.NORMAL.id;
		String lateTime = "";
		String leaveTime = "";
		double workOuttime = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		//判断是否为工作日
		boolean isWorkingDay = holidayCacheService.getTypeFromCache(attendanceDate.toString()) <= 0;
		if (isWorkingDay) {
			if (StringUtils.isEmpty(inTime) || StringUtils.isEmpty(outTime)) {
				flag = AttendanceStatus.INCOMPLETE.id;
			} else {
				//迟到
				Date standardStartTimeLong = sdf.parse(PropertyUtil.get("standard.starttime"));
				Date intTimeLong = sdf.parse(inTime);
				int lateTimeLong = (int) ((intTimeLong.getTime() - standardStartTimeLong.getTime()) / 1000 / 60);
				lateTime = lateTimeLong > 0 ? Integer.toString(lateTimeLong+5) : "0";

				//早退
				Date standardEndTimeLong = sdf.parse(PropertyUtil.get("standard.endtime"));
				Date outTimeLong = sdf.parse(outTime);
				int leaveTimeLong = (int) ((standardEndTimeLong.getTime() - outTimeLong.getTime()) / 1000 / 60);
				leaveTime = leaveTimeLong > 0 ? Integer.toString(leaveTimeLong) : "0";

				//加班  eg: 100/60.0
				workOuttime = (leaveTimeLong > 0 ? 0 : Math.abs(leaveTimeLong)) / 60.0;
				workOuttime = workOuttime < 0.5 ? 0 : workOuttime; //如果小于半个小时，则记0

				if(leaveTimeLong > 0 && lateTimeLong > 0) {
					flag = AttendanceStatus.LATEANDLEAVE.id;
				} else if(lateTimeLong > 0 && leaveTimeLong <= 0){
					flag = AttendanceStatus.LATE.id;
				} else if(lateTimeLong <= 0 && leaveTimeLong > 0){
					flag = AttendanceStatus.LEAVE.id;
				} else {
					flag = AttendanceStatus.NORMAL.id;
				}
			}
		} else { 
			if (StringUtils.isEmpty(inTime) && StringUtils.isEmpty(outTime)) {
				flag = AttendanceStatus.NORMAL.id;
			} else if (StringUtils.isEmpty(inTime) || StringUtils.isEmpty(outTime)){
				flag = AttendanceStatus.INCOMPLETE.id;
			} else {
				// 节假日加班
				flag = AttendanceStatus.HOLIDAYWORKING.id;
				workOuttime =  new BigDecimal((sdf.parse(outTime).getTime() - sdf.parse(inTime).getTime()) / 1000.0 / 60 / 60)
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				if(workOuttime <= 4)
					flag = AttendanceStatus.HOLIDAYWORKINGHALF.id;
			}
		}

		map.put("LateTime", lateTime);
		map.put("LeaveTime", leaveTime);
		map.put("WorkOuttime", workOuttime);
		map.put("Flag", flag);
		return map;
	}

	@Override
	public List<AttendanceRecord> getAll() {
		return repository.findAll();
	}

	@Override
	public Page<AttendanceRecord> getAttendancesByPage(int pageNumber, int pageSize, String name, String startDate,
			String endDate, String isOnJob) {
		List<AttendanceRecord> list = findByQuery(name, startDate, endDate, isOnJob, entityManager);
		//获取分页信息
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPage(pageNumber);
		pageQuery.setSize(pageSize);
		Pageable pageable = DataHelper.toPageable(pageQuery);
		//分页显示
		List<AttendanceRecord> anyList = new ArrayList<AttendanceRecord>();
		int max = pageQuery.getPage() * pageQuery.getSize();
		if (max >= list.size()) {
			max = list.size();
		}
		for (int i = (pageQuery.getPage() - 1) * pageQuery.getSize(); i < max; i++) {
			anyList.add(list.get(i));
		}
		Page<AttendanceRecord> sourceCodes = new org.springframework.data.domain.PageImpl<>(anyList, pageable,
				list.size());
		return sourceCodes;
	}

	@Override
	public AttendanceRecord modify(AttendanceRecord entity) {
		return repository.saveAndFlush(entity);
	}
	
	@SuppressWarnings("unchecked")
	private List<AttendanceRecord> findByQuery(String name, String startDate, String endDate, String isOnJob, EntityManager entityManager) {
		StringBuilder stb = new StringBuilder();
		stb.append("select new com.primeton.manage.employee.entity.AttendanceRecord(ar.id as id, ar.attendanceDate as attendanceDate, ar.todayWeek as todayWeek,"
				+ "  ar.signinTime as signinTime, ar.signoutTime as signoutTime, ei.name as empName,"
				+ " ar.vacationHour as vacationHour, ar.lateTime as lateTime, ar.leaveTime as leaveTime,ar.workOuttime as workOuttime, ar.flag as flag,ar.empId as empId"
				+ ") from EmployeeInfo ei, AttendanceRecord ar where ei.id=ar.empId ");
		if (!"".equals(name) && name != null && !"null".equals(name)) {
			stb.append(" and ei.name like '%").append(name).append("%' ");
		} 
		if (!"".equals(startDate) && startDate != null && !"null".equals(startDate)) {
			stb.append(" and ar.attendanceDate >='").append(startDate).append("'");
		} 
		if (!"".equals(endDate) && endDate != null && !"null".equals(endDate)) {
			stb.append(" and ar.attendanceDate <='").append(endDate).append("'");
		} 
		if (!"".equals(isOnJob) && isOnJob != null && !"null".equals(isOnJob)) {
			stb.append(" and ar.flag = ").append(isOnJob);
		}
		
		stb.append("order by MONTH (ar.attendanceDate) DESC, ar.empId, DAY(ar.attendanceDate)");
		
		return entityManager.createQuery(stb.toString()).getResultList();
	}
	
}
