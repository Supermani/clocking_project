package com.primeton.manage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载工具类
 * @author admin
 *
 */
public class FileUploadUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param uuid
	 * @return
	 */
	public ResultUtil fileDownload(HttpServletRequest request, HttpServletResponse response ,String uuid, String path, String fileName) {
		ResultUtil result = new ResultUtil();
		try {
			String allpath = path + "/"+fileName;
			File file = new File(allpath);

			if( !file.exists()){
				result.setStatus(1);
				result.setData("file not exists !");
				System.out.println(allpath + " is not exists !!!!!!!!!!!!!");
				return result;
			}
			
			// 解决 苹果浏览器乱码问题 -
			String ua = request.getHeader("User-Agent").toLowerCase();
			String loadname = "";
			if (ua.indexOf("macintosh") > -1 && ua.indexOf("chrome") < 0) {
				loadname = new String(file.getName().getBytes(), "iso8859-1");
			} else if (ua.indexOf("firefox") > 0) {
				//loadname ="=?UTF-8?B?" + (new BASE64Encoder().encode(file.getName().getBytes("UTF-8"))) + "?=";
				loadname = "=?UTF-8?B?" + (new String(Base64.getEncoder().encode(file.getName().getBytes("UTF-8"))))
						+ "?=";
			} else {
				loadname = URLEncoder.encode(file.getName(), "utf-8");
			} 

			// 重置输出流
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + loadname); // 设置文件名
			response.addHeader("Content-Length", file.length() + ""); // 设置下载文件大小
			response.setContentType("multipart/form-data;charset=utf-8"); // 设置文件类型
			// 读取文件数据
			FileInputStream fis = new FileInputStream(allpath);
			OutputStream outPutStream = response.getOutputStream();
			if (fis.available() != 0) {
				response.setStatus(200);
			}
			IOUtils.copy(fis, outPutStream);
			fis.close();
			response.flushBuffer();
			response.getOutputStream().close();
			result.setStatus(0);
		} catch (IOException e) {
			result.setStatus(1);
			result.setData("文件下载失败！");
			System.out.println(fileName + "文件下载失败！" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @return
	 */
	public ResultUtil fileUpload(MultipartFile file, String path) {
		ResultUtil result = new ResultUtil();
		String fileName = file.getOriginalFilename();
		String type = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		String fileNo = UUID.randomUUID().toString();
		
		// 判断上传文件类型是否被允许
		String[] typeNames = getAllowedFileTypes().split(";");
		boolean bOK = false;
		for (String typename : typeNames) {
			if (fileName.endsWith(typename)) {
				bOK = true;
			}
		}
		// 后缀不合法, 直接结束
		if (!bOK) {
			result.setStatus(1);
			result.setMsg("文件类型不合法");
			return result;
		}
		String fileNameMd5 = fileName.substring(0, fileName.lastIndexOf(".")) +"_"+ System.currentTimeMillis()+"."+type;
		File targetFile = new File(path, fileNameMd5);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);// 保存
			result.setStatus(0);// 成功
			result.setMsg(fileName + "文件上传成功");
			result.setData(fileNo);
		} catch (Exception e) {
			result.setStatus(200);
			result.setMsg(fileName + "文件上传失败！");
			logger.error(fileName + "文件上传失败！" + e.getMessage());
		}
		return result;
	}
	
	private String getAllowedFileTypes() {
		return "xlsx;xls";
	}
}
