// @formatter:off
package com.everhomes.rest.express;

public interface ExpressServiceErrorCode {
	String SCOPE = "express";
	
	long MODULE_CODE = 40700;  //快递模块id
	int STATUS_ERROR = 10000;  //订单状态错误
	int PRIVILEGE_ERROR = 10001;  //您没有相关权限
	int NOT_SIGNED_USER_ERROR = 10002;  //您没有相关权限
	int EMPTY_WORK_FLOW = 180806;  //没有启用工作流
	int ERROR_CREATE_FLOWCASE = 180807;  //创建工作流失败

	public static final String PAYSERVER_URL ="guomao.payserver.url";
	public static final String OFFICIAL_ACCOUNTS_PAYSERVER_URL ="guomao.official.accounts.payserver.url";//公众号支付
	
    public final static String USER_PROFILE_KEY = "expressGuomaoUser";
}
