package com.primeton.manage.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtils {
	private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

	@SuppressWarnings("deprecation")
	public static String parseExcel(Cell cell) {
		String result = new String();
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型  
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else {// 日期  
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				result = sdf.format(date);
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			} else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat format = new DecimalFormat();
				String temp = style.getDataFormatString();
				// 单元格设置成常规  
				if (temp.equals("General")) {
					format.applyPattern("#");
				}
				result = format.format(value);
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:// String类型  
			result = cell.getRichStringCellValue().toString();
			if(result.contains("/")){
				// LocalDate.parse(result, DateTimeFormatter.ofPattern("yyyy/M/d")).toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				try {
					result = sd.format(sdf.parse(result)).toString();
				} catch (ParseException e) {
					logger.error("解析日期" + result + "失败！");
				}
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
		default:
			result = "";
			break;
		}
		return result;
	}
}
