package com.primeton.manage.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 加载配置文件
 * @author zhangyingwen
 *
 */
public class PropertyUtil {
	
	private static Logger logger = LogManager.getLogger(PropertyUtil.class);
	private static Properties pro;
	
	private PropertyUtil(){
		
	}
	/**
	 * 获得连接
	 * @param key
	 * @return
	 */
	public static String get(String key){
		if(pro == null){
			init();
		}
		return pro.getProperty(key);
	}

	private static void init() {
		pro = new Properties();
		try {
			pro.load(PropertyUtil.class.getClassLoader().getResourceAsStream("parameters.properties"));
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
}
