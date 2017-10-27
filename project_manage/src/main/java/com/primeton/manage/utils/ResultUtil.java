package com.primeton.manage.utils;
/**
 * 作为服务器返回json格式的结果对象
 * @author admin
 *
 */
public class ResultUtil {
	private int status;//返回的状态  0 失败或者异常, 1正常
	private String msg;//返回的消息
	private Object data;//返回的数据
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public ResultUtil() {
	}
	
	
	public ResultUtil(int status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	
}
