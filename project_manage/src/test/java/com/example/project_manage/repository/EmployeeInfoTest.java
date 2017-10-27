
package com.example.project_manage.repository;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.primeton.manage.ProjectManageApplication;
import com.primeton.manage.employee.dao.AttendanceRepository;
import com.primeton.manage.employee.dao.EmployeeInfoRepository;
import com.primeton.manage.employee.entity.AttendanceRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManageApplication.class)
public class EmployeeInfoTest {

	@Autowired
	EmployeeInfoRepository employeeInfoRepository;

	@Autowired
	AttendanceRepository attendanceRepository;

	public void all() throws Exception {
		System.out.println(employeeInfoRepository.findAll());
	}

	public void count() {
		System.out.println(attendanceRepository.findAttendanceDays(15326L, "2017-05-01", "2017-05-31"));
	}

	public void page() {
		System.out.println(attendanceRepository.findAll(new PageRequest(1, 10)));
	}

	@Test
	public void modify() {
		System.out.println(attendanceRepository.saveAndFlush(new AttendanceRecord(3L, 15326L, Date.valueOf(LocalDate.now()), "星期三",
				"08:51", "19:33")));
	}
}
