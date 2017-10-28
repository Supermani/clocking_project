package com.primeton.manage.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Sun
 * @Date: 下午11:47 2017/10/28
 * @Description:
 */
public class DateUtils {

    /**
     * 日期转成字符串
     * @param date
     * @return
     */
    public static String getDate2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getNowStr() {
        return getDate2Str(new Date());
    }
}
