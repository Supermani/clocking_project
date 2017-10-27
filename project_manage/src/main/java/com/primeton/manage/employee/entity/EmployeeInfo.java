package com.primeton.manage.employee.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 人员信息表
 * @author admin
 *
 */

@Entity
@Table(name = "employee_info")
public class EmployeeInfo implements Serializable{

	@Id
	public Long id;

	@Column(nullable = false, unique = true)
	public String name;

	@Column(nullable = false)
	public Timestamp inductionTime = new Timestamp(System.currentTimeMillis()); //入职时间

	@Column
	public Timestamp resignationTime; //离职时间

	@Column(nullable = false)
	public Integer isOnJob; //是否在职

	public EmployeeInfo() {

	}

	public EmployeeInfo(Long id, String name, Timestamp inductionTime, Timestamp resignationTime, Integer isOnJob) {
		super();
		this.id = id;
		this.name = name;
		this.inductionTime = inductionTime;
		this.resignationTime = resignationTime;
		this.isOnJob = isOnJob;
	}
	
	public EmployeeInfo(Long id, String name, Date inductionTime, Date resignationTime, int isOnJob) {
		this.id = id;
		this.name = name;
		this.inductionTime = (Timestamp) inductionTime;
		this.resignationTime = (Timestamp) resignationTime;
		this.isOnJob = isOnJob;
	}

	@Override
	public String toString() {
		return "EmployeeInfo [id=" + id + ", name=" + name + ", inductionTime=" + inductionTime + ", resignationTime="
				+ resignationTime + ", isOnJob=" + isOnJob + "]";
	}
	
	public String toJson() {
		return JSON.toJSONString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getInductionTime() {
		return inductionTime;
	}

	public void setInductionTime(Timestamp inductionTime) {
		this.inductionTime = inductionTime;
	}

	public Timestamp getResignationTime() {
		return resignationTime;
	}

	public void setResignationTime(Timestamp resignationTime) {
		this.resignationTime = resignationTime;
	}

	public Integer getIsOnJob() {
		return isOnJob;
	}

	public void setIsOnJob(Integer isOnJob) {
		this.isOnJob = isOnJob;
	}
}
