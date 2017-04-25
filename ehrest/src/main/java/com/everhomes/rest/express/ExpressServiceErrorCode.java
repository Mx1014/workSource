// @formatter:off
package com.everhomes.rest.express;

public interface ExpressServiceErrorCode {
	String SCOPE = "express";
	
	int STATUS_ERROR = 10000;  //订单状态错误
	int PRIVILEGE_ERROR = 10001;  //您没有相关权限
}
