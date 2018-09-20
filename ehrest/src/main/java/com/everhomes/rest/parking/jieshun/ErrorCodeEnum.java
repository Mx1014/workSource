package com.everhomes.rest.parking.jieshun;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum ErrorCodeEnum {

	/* 0～999 系统异常 */
	SUCCESS(0, "没有异常"), 
	UNKNOWN_ERR(1, "未知异常"),
	PARAM_P_NOT_COMPLETE(2, "P参数不完整"),
	USER_NOT_EXIST(3, "用户不存在"),
	USER_IS_FOBBIDEN(4, "用户已经禁用"),
	PWD_NOT_RIGHT(5, "密码不正确"),
	TOKEN_OUTDATED_OR_INVALID(6, "无效的令牌或令牌已过期"),
	SIGNATURE_INVALID(7, "无效的数据签名"),
	REQUEST_API_NOT_AUTH(8, "访问的功能未经授权"),
	REQUEST_DATA_NOT_AUTH(9, "访问的数据未经授权"),
	CID_NOT_EXIST(13, "客户号不存在"),
	CID_NOT_VALID(14, "客户号无效"),
	VERSION_NOT_RIGHT(15, "接口版本不正确"),
	PARAM_P_MISSING(16, "缺少参数p"),
	SERVICE_STOPPING_RIGHT_NOW(17, "服务暂停，请稍后"),
	SERVICE_ID_NOT_EXIST(100, "调用了不存在的方法（即serviceId不存在）"),

	/* 1000以上是业务异常 */
	BUSINESSER_NOT_EXIST(2087, "商户不存在"),
	
	NONE(-9999, "");
	
	public static int MAX_SYS_ERR_NUM = 999; // 最大的系统异常编
	
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
