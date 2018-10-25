// @formatter:off
package com.everhomes.rest.express;

public interface ExpressServiceErrorCode {
	String SCOPE = "express";
	
	long MODULE_CODE = 40700;  //快递模块id
	int STATUS_ERROR = 10000;  //订单状态错误
	int PRIVILEGE_ERROR = 10001;  //您没有相关权限	
	int NOT_SIGNED_USER_ERROR = 10002;  
	
	int URL_ENCODE_ERROR = 10003;  //URL加密失败
	int HTTP_REQUEST_ERROR = 10004;  //请求失败
	int INPUT_PARAM_LACK = 10005;  //接口参数缺失
	int INPUT_PARAM_INVALID = 10006;  //接口参数异常
	int ORDER_NOT_EXIST = 10007;  //订单不存在
	int GET_COMPANY_FAILED = 10008;  //获取公司失败
	int LOGIN_SIGNED_FAILED = 10009;  //用户鉴权失败，请重新登录
	int ORDER_ABNORMAL = 10010;  //订单异常
	int ERROR_FROM_THIRD = 10011;  //第三方返回失败
	int PAY_CHECK_SIGNED_FAILED = 10012;  //支付鉴权失败
	
	int EMPTY_WORK_FLOW = 180806;  //没有启用工作流
	int ERROR_CREATE_FLOWCASE = 180807;  //创建工作流失败
	int ERROR_CREATE_USER_ACCOUNT = 180808;  //创建个人付款方失败
	int ERROR_CREATE_REPEAT_PAYEE_ACCOUNT = 180809; //该收款账户已存在，请勿重复添加
	int ERROR_PAYEE_ACCOUNT_ID_NOT_EXIST = 180810; //需要更新的收款账户ID不存在

	String WORK_FLOW_NAME = "workflowtitle";  //工作流名称

	public static final String PAYSERVER_URL ="guomao.payserver.url";
	public static final String OFFICIAL_ACCOUNTS_PAYSERVER_URL ="guomao.official.accounts.payserver.url";//公众号支付
	
    public final static String USER_PROFILE_KEY = "expressGuomaoUser";
}
