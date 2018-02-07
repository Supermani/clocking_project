package com.primeton.manage.employee.controller;

import com.primeton.manage.statistics.AttendanceCalculation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2017/11/6.
 */
@Controller
@RequestMapping(value = "/export")
public class ExportDataController {

    private static Logger logger = LoggerFactory.getLogger(ExportDataController.class);

    @Autowired
    private AttendanceCalculation attendanceCalculation;

    /**
     * 导出 加班费统计
     * @param request
     * @param response
     * @param date
     */
    @GetMapping(value="overtimeCost")
    public void exportOvertimeCostCalcInfo(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(name = "date", required = false) String date){

        File file = new File("OvertimeCostStatistics_"+date.substring(0, 7)+".csv");
        try (PrintWriter pw = new PrintWriter(file); FileInputStream fis = new FileInputStream(file)) {
            StringBuilder sb = new StringBuilder();
            sb.append("考勤号码").append(",").append("姓名").append(",").append("加班总小时数").append(",").append("加班费用")
                    .append(",").append("餐补天数").append(",").append("餐补费用");
            pw.println(sb.toString());
            List<Map<String ,Object>> list = attendanceCalculation.overtimeCostCalc(date);
            for (Map<String, Object> obj : list) {
                sb.setLength(0);
                sb.append(obj.get("Id")).append(",").append(obj.get("Name")).append(",").append(obj.get("Overtimes"))
                        .append(",").append(obj.get("OvertimeCost")).append(",").append(obj.get("MealDay"))
                        .append(",").append(obj.get("MealCost"));

                pw.println(sb.toString());
            }

            pw.flush();
            OutputStream outPutStream = response.getOutputStream();

            if (fis.available() != 0) {
                response.setStatus(200);
            }
            //重置输出流
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName()); //设置文件名
            response.addHeader("Content-Length", String.valueOf(file.length())); //设置下载文件大小
            response.setContentType("application/vnd.ms-excel;charset=utf-8"); //设置文件类型
            IOUtils.copy(fis, outPutStream);
        } catch (Exception e) {
            logger.error("导出加班费用统计错误！",e);
        }
        return;
    }

}
