package com.primeton.manage.employee.controller;

import com.primeton.manage.employee.entity.AttendanceRecord;
import com.primeton.manage.employee.service.AttendanceRecordService;
import com.primeton.manage.employee.service.EmployeeInfoService;
import com.primeton.manage.statistics.AttendanceCalculation;
import com.primeton.manage.utils.ConstUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/clocking")
public class AttendanceRecordController {
	private static Logger logger = LoggerFactory.getLogger(AttendanceRecordController.class);

	@Resource
	private AttendanceRecordService service;

	@Resource
	private EmployeeInfoService employeeInfoService;
	
	@Autowired
	private AttendanceCalculation attendanceCalculation;

	@PostMapping(value = "import")
	public String goToEmpinfo(MultipartHttpServletRequest multiReq) {

		service.importRecord(multiReq);

		return "redirect:/clocking/index";
	}

	@GetMapping(value = "index")
	public String index(HttpServletRequest request, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			@RequestParam(name = "isOnJob", required = false) String isOnJob,
			@RequestParam(name = "pageNumber", required = false) String pageNumberStr, Model model) {

		if (pageNumberStr == null || "".equals(pageNumberStr)) {
			pageNumberStr = "1";
		}

		int pageNumber = Integer.parseInt(pageNumberStr);
		int pageSize = ConstUtils.PAGE_SIZE;

		Page<AttendanceRecord> page = service.getAttendancesByPage(pageNumber, pageSize, name, startDate, endDate,
				isOnJob);
		model.addAttribute("page", page);

		if (page.isFirst()) {
			model.addAttribute("first", true);
		}

		if (page.isLast()) {
			model.addAttribute("last", true);
		}
		model.addAttribute("name", name);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("isOnJob", isOnJob);

		String url = request.getRequestURI() + "?" + request.getQueryString();
		if (StringUtils.isEmpty(request.getQueryString())) {
			url = request.getRequestURI() + "?";
		}
		url = StringUtils.substringBefore(url, "&pageNumber");
		model.addAttribute("queryUrl", url);
		return "attendance/index";
	}

    @PostMapping(value = "modify")
    public String modify(HttpServletRequest request, AttendanceRecord attendanceRecord) {
        service.modify(attendanceRecord);
        String referer = request.getHeader("Referer");
        String host = request.getHeader("Host");
        String uri = StringUtils.substringAfter(referer, host);
        return "redirect:" + uri;
    }

	@GetMapping(value = "export")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			@RequestParam(name = "isOnJob", required = false) String isOnJob,
			@RequestParam(name = "pageNumber", required = false) String pageNumberStr) {
		if (pageNumberStr == null || "".equals(pageNumberStr)) {
			pageNumberStr = "1";
		}

		int pageNumber = Integer.parseInt(pageNumberStr);
		int pageSize = Integer.MAX_VALUE;

		System.out.println("----------------------startDate is " + startDate);
		
		File file = new File("PrimetonRecord_"+ startDate.substring(0, 7)+".csv");
		try (PrintWriter pw = new PrintWriter(file)) {
			StringBuilder sb = new StringBuilder();
			sb.append("考勤号码").append(",").append("姓名").append(",").append("星期").append(",").append("日期").append(",")
				.append("签到时间").append(",").append("签退时间").append(",").append("休假(小时)").append(",").append("	迟到(分钟)")
				.append(",").append("早退(分钟)").append(",").append("加班(小时)").append(",");
			pw.println(sb.toString());
			
			Page<AttendanceRecord> page = service.getAttendancesByPage(pageNumber, pageSize, name, startDate, endDate, isOnJob);
			List<AttendanceRecord> list = page.getContent();
			for (AttendanceRecord ar : list) {
				sb.setLength(0);
				sb.append(ar.getEmpId()).append(",").append(ar.empName).append(",").append(ar.getTodayWeek())
						.append(",").append(new SimpleDateFormat("yyyy-MM-dd").format(ar.getAttendanceDate())).append(",")
						.append(ar.getSigninTime()).append(",").append(ar.getSignoutTime()).append(",")
						.append(ar.getVacationHour()).append(",").append(ar.getLateTime()).append(",")
						.append(ar.getLeaveTime()).append(",").append(ar.getWorkOuttime());

				pw.println(sb.toString());
			}

			pw.flush();
			OutputStream outPutStream = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);
			if (fis.available() != 0) {
				response.setStatus(200);
			}
			//重置输出流
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + file.getName()); //设置文件名
			response.addHeader("Content-Length", String.valueOf(file.length())); //设置下载文件大小
			response.setContentType("application/vnd.ms-excel;charset=utf-8"); //设置文件类型
			IOUtils.copy(fis, outPutStream);
		} catch (Exception e) {
			logger.error("导出普元考勤信息错误！",e);
		}
		return;
	}
	
	@GetMapping(value="exportMonthly")
	public void exportMonthlyCalcInfo(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			@RequestParam(name = "isOnJob", required = false) String isOnJob){
		
		System.out.println("----------------------startDate is " + startDate);
		
		File file = new File("PrimetonStatis_"+startDate.substring(0, 7)+".csv");
		try (PrintWriter pw = new PrintWriter(file)) {
			StringBuilder sb = new StringBuilder();
			sb.append("考勤号码").append(",").append("姓名").append(",").append("工作日出勤天数").append(",").append("人月数")
				.append(",").append("节假日加班天数").append(",").append("工作日加班小时数");
			pw.println(sb.toString());
			List<Map<String ,Object>> list = attendanceCalculation.monthlyAttendanceCalc(startDate, endDate);
			for (Map<String, Object> obj : list) {
				sb.setLength(0);
				sb.append(obj.get("Id")).append(",").append(obj.get("Name")).append(",").append(obj.get("MonthlyAttendanceDay"))
				.append(",").append(obj.get("MonthlyRadio")).append(",").append(obj.get("HolidayWorkingDays"))
				.append(",").append(obj.get("WorkOverTimes"));
				
				pw.println(sb.toString());
			}

			pw.flush();
			OutputStream outPutStream = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);
			if (fis.available() != 0) {
				response.setStatus(200);
			}
			//重置输出流
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + file.getName()); //设置文件名
			response.addHeader("Content-Length", String.valueOf(file.length())); //设置下载文件大小
			response.setContentType("application/vnd.ms-excel;charset=utf-8"); //设置文件类型
			IOUtils.copy(fis, outPutStream);
		} catch (Exception e) {
			logger.error("导出普元月考勤统计错误！",e);
		}
		return;
	}

}
