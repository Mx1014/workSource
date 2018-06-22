package com.everhomes.rest.configurations.admin;

/**
 * 配置项错误码声明接口
 * @author huanglm(motto)
 *
 */
public interface ConfigurationsErrorCode {

	/**
	 * 域
	 */
	static final String SCOPE = "configurations";
	/**
	 * 数据重复错误码
	 */
	static final int ERROR_CODE_MULTIPLE = 10001;
	/**
	 * 数据只读错误码
	 */
	static final int ERROR_CODE_READONLY = 10002;
}
