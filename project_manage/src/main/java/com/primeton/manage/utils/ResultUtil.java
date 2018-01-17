package com.primeton.manage.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作为服务器返回json格式的结果对象
 * @author admin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUtil {

	private int status;//返回的状态  0 失败或者异常, 1正常

	private String msg;//返回的消息

	private Object data;//返回的数据
}
