package com.primeton.manage.employee.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员信息表
 * @author admin
 *
 */

@Entity
@Table(name = "employee_info")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

	public String toJson() {
		return JSON.toJSONString(this);
	}

}
