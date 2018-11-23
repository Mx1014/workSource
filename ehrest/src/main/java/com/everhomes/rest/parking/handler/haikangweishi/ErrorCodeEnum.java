package com.everhomes.rest.parking.handler.haikangweishi;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum ErrorCodeEnum {

	SUCCESS(0, "接口调用成功，并正常返回"), 
	SYSTEM_ERR(1000, "系统异常"),
	API_PARAM_NOT_VALID(1001, "API参数无效"),
	APPKEY_NOT_VALID(1002, "appkey无效"),
	TIME_NOT_VALID(1003, "time时间戳无效"),
	TOKEN_NOT_VALID(1004, "token无效"),
	SERVICE_ABNORAML(1005, "平台服务异常"),
	API_INTERFACE_ABNORAML(1006, "API接口异常"),
	VERSION_NOT_MATCH(1007, "版本不兼容"),
	INNER_METHOD_TIMEOUT(1008, "内部接口调用超时"),
	INNER_API_NOT_EXIST(1009, "内部接口不存在"),
	NONE(-9999, "");
	
	private Integer code;
	private String info;

	private ErrorCodeEnum(Integer code, String info) {
		this.code = code;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getInfo() {
		return info;
	}

	public Integer getCode() {
		return code;
	}
}
