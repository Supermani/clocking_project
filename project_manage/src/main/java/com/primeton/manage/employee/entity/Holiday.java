package com.primeton.manage.employee.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "holiday")
public class Holiday {
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Column
	public Date date;
	
	@Column
	public Integer dateType; //2节假日,1休息日

	public Holiday(){
	}
	
	public Holiday(Date date, Integer dateType) {
		super();
		this.date = date;
		this.dateType = dateType;
	}
	
}
