package com.primeton.manage.employee.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.primeton.manage.employee.entity.EmployeeInfo;
import com.primeton.manage.employee.service.EmployeeInfoService;
import com.primeton.manage.utils.ConstUtils;

@Controller
@RequestMapping("/emp")
public class EmployeeInfoController {

	private static Logger logger = LoggerFactory.getLogger(EmployeeInfoController.class);

	@Resource
	private EmployeeInfoService employeeInfoService;

	@GetMapping(value = "index")
	public String goToEmpinfo(HttpServletRequest request,@RequestParam(name="name", required=false) String name,
			@RequestParam(name="startDate", required=false) String startDate,
			@RequestParam(name="endDate", required=false) String endDate,
			@RequestParam(name="isOnJob", required=false) String isOnJob,
			@RequestParam(name="pageNumber", required=false) String pageNumberStr,Model model) {

		if (pageNumberStr == null || "".equals(pageNumberStr)) {
			pageNumberStr = "1";
		}
		
		int pageNumber = Integer.parseInt(pageNumberStr);
		int pageSize = ConstUtils.PAGE_SIZE;

		Page<EmployeeInfo> page = employeeInfoService.getEmpsByPage(pageNumber, pageSize, name, startDate, endDate, isOnJob);
		model.addAttribute("page", page);
		
		if(page.isFirst()){
			model.addAttribute("first", true);
		}
		
		if(page.isLast()){
			model.addAttribute("last", true);
		}
		model.addAttribute("name", name);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("isOnJob", isOnJob);
		
		String url = request.getRequestURI() +"?"+ request.getQueryString();
		if(StringUtils.isEmpty(request.getQueryString())){
			url =  request.getRequestURI() +"?";
		}
		url = StringUtils.substringBefore(url, "&pageNumber");
		model.addAttribute("queryUrl", url);
		return "employee/empInfo";
	}
	
	@RequestMapping(value="delete")
	public String delete(@RequestParam(name="id", required=false) String id) {
		employeeInfoService.delete(id);
		return "redirect:/emp/index";
	}
	
	@RequestMapping("/add")
	public String add(@RequestParam(name="empNumber", required=false) String empNumber,
			@RequestParam(name="empName", required=false) String empName) {
		employeeInfoService.add(empNumber, empName);
		return "redirect:/emp/index";
	}

	@RequestMapping("/edit")
	public String edit(
					   @RequestHeader("Referer") String referer,
					   @RequestHeader("Host") String host,
					   @RequestParam(name="id", required=false) String id,
					   @RequestParam(name="empName", required=false) String name,
					   @RequestParam(name="startDate", required=false) String startDate,
					   @RequestParam(name="endDate", required=false) String endDate,
					   @RequestParam(name="isOnJob", required=false) String isOnJob) {
		employeeInfoService.edit(id, name, startDate, endDate, isOnJob);

		String uri = StringUtils.substringAfter(referer, host);
		return "redirect:" + uri;
	}
}
