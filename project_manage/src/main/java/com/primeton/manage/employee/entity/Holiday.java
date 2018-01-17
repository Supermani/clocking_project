package com.primeton.manage.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "holiday")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Holiday {
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Column
	public Date date;
	
	@Column
	public Integer dateType; //2节假日,1休息日
	
}
