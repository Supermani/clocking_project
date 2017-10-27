package com.primeton.manage.employee.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

@Entity
@Table(name = "attendance_record")
public class AttendanceRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	public Long id;
	
	@Column(nullable = false)
	public Long empId;
	
	@Column(nullable = false)
	public Date attendanceDate; // 考勤日期
	
	@Column(nullable = false)
	public String todayWeek;
	
	@Column 
	public String signinTime; // 签到时间
	
	@Column 
	public String signoutTime; // 签退时间
	
	@Column(nullable = false)
	public int vacationHour = 0; // 请假时长
	
	@Column
	public String lateTime; //迟到
	
	@Column
	public String leaveTime; //早退
	
	@Column(nullable = false)
	public double workOuttime = 0; //加班
	
	@Column(nullable = false)
	public int deduction = 0; // 是否抵扣节假日加班调休（0不抵扣，1抵扣，2抵扣半天）
	
	@Column(nullable = false)
	public int flag = 0; //打卡是否正常(0正常，1迟到，2早退，3即迟到又早退，4打卡记录不完整)
	
	public String empName;
	
	public AttendanceRecord(){
		
	}

	public AttendanceRecord(Long empId, Date attendanceDate, String todayWeek, String signinTime,
			String signoutTime) {
		super();
		this.empId = empId;
		this.attendanceDate = attendanceDate;
		this.todayWeek = todayWeek;
		this.signinTime = signinTime;
		this.signoutTime = signoutTime;
	}
	
	public AttendanceRecord(Long id, Long empId, Date attendanceDate, String todayWeek, String signinTime,
			String signoutTime) {
		super();
		this.id = id;
		this.empId = empId;
		this.attendanceDate = attendanceDate;
		this.todayWeek = todayWeek;
		this.signinTime = signinTime;
		this.signoutTime = signoutTime;
	}
	
	public AttendanceRecord(Long empId, Date attendanceDate, String todayWeek, String signinTime, String signoutTime,
			String lateTime, String leaveTime, double workOuttime, int flag) {
		super();
		this.empId = empId;
		this.attendanceDate = attendanceDate;
		this.todayWeek = todayWeek;
		this.signinTime = signinTime;
		this.signoutTime = signoutTime;
		this.lateTime = lateTime;
		this.leaveTime = leaveTime;
		this.workOuttime = workOuttime;
		this.flag = flag;
	}
	
	public AttendanceRecord(long id, java.util.Date attendanceDate, String todayWeek, String signinTime, String signoutTime,String empName,
			int vacationHour, String lateTime, String leaveTime, double workOuttime, int flag, Long empId) {
		this.id = id;
		this.attendanceDate = (Date) attendanceDate;
		this.todayWeek = todayWeek;
		this.signinTime = signinTime;
		this.signoutTime = signoutTime;
		this.empName = empName;
		this.vacationHour = vacationHour;
		this.lateTime = lateTime;
		this.leaveTime = leaveTime;
		this.workOuttime = workOuttime;
		this.flag = flag;
		this.empId = empId;
		
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = Date.valueOf(attendanceDate);
	}

	public String getTodayWeek() {
		return todayWeek;
	}

	public void setTodayWeek(String todayWeek) {
		this.todayWeek = todayWeek;
	}

	public String getSigninTime() {
		return signinTime;
	}

	public void setSigninTime(String signinTime) {
		this.signinTime = signinTime;
	}

	public String getSignoutTime() {
		return signoutTime;
	}

	public void setSignoutTime(String signoutTime) {
		this.signoutTime = signoutTime;
	}

	public int getVacationHour() {
		return vacationHour;
	}

	public void setVacationHour(int vacationHour) {
		this.vacationHour = vacationHour;
	}

	public String getLateTime() {
		return lateTime;
	}

	public void setLateTime(String lateTime) {
		this.lateTime = lateTime;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public double getWorkOuttime() {
		return workOuttime;
	}

	public void setWorkOuttime(double workOuttime) {
		this.workOuttime = workOuttime;
	}

	public int getDeduction() {
		return deduction;
	}

	public void setDeduction(int deduction) {
		this.deduction = deduction;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

}
