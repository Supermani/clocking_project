package com.primeton.manage.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 打卡记录标记枚举类
 *
 * @author admin
 */
public enum AttendanceStatus {
    // 利用构造函数传参
    NORMAL(0, "正常"),
    LATE(1, "迟到"),
    LEAVE(2, "早退"),
    LATEANDLEAVE(3, "即迟到又早退"),
    INCOMPLETE(4, "打卡记录不完整"),
    HOLIDAYWORKING(5, "节假日加班"),
    HOLIDAYWORKINGHALF(6, "节假日加班半天"),
    LEAVE_APPLICATION(7, "请假"),
    EXCEPTION(8, "异常");

    // 定义私有变量
    public Integer id;
    public String explain;

    // 构造函数，枚举类型只能为私有
    private AttendanceStatus(int id, String explain) {
        this.id = id;
        this.explain = explain;
    }

    public int getId() {
        return id;
    }

    public String getExplain() {
        return explain;
    }

    // 用于页面搜索的类型
    public static List<AttendanceStatus> allAttendance = Arrays.asList(
            NORMAL,
            LATE,
            LEAVE,
            LATEANDLEAVE,
            INCOMPLETE,
            HOLIDAYWORKING,
            HOLIDAYWORKINGHALF,
            LEAVE_APPLICATION,
            EXCEPTION);

    @Override
    public String toString() {
        return this.explain;
    }

    public static void main(String[] args) {

        System.out.println(AttendanceStatus.LATE);
    }
}
