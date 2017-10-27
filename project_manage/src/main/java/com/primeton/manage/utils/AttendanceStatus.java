package com.primeton.manage.utils;

/**
 * 打卡记录标记枚举类
 * @author admin
 *
 */
public enum AttendanceStatus {
	// 利用构造函数传参
	NORMAL(0, "正常"), 
	LATE(1, "迟到"), 
	LEAVE(2, "早退"), 
	LATEANDLEAVE(3, "即迟到又早退"), 
	INCOMPLETE(4,"打卡记录不完整"),
	HOLIDAYWORKING(5,"节假日加班"),
	HOLIDAYWORKINGHALF(6,"节假日加班半天");

	// 定义私有变量
	public int id;
	public String explain;
	// 构造函数，枚举类型只能为私有
	private AttendanceStatus(int id,String explain) {
		this.id = id;
		this.explain = explain;
	}

	@Override
	public String toString() {
		return this.explain;
	}
	
	public static void main(String[] args) {
		System.out.println(AttendanceStatus.LATE);
	}
}
