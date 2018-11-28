package com.everhomes.officecubicle;

public interface OfficeCubicleErrorCode {

	String SCOPE = "officecubicle";
    int ERROR_EXIST_CITY = 10001;  //城市已存在
	int ERROR_CITY_ID = 10002;//城市id不存在
	int ERROR_EMPTY_CITYS = 10003;//城市名称为空
	int ERROR_DELETE_CITYS = 10004;//城市名称为空
	int ERROR_UNENABLE_FLOW = 10005;//工作流未启用

	int ERROR_AlREADY_CUSTOMIZE_CONFIG = 10006;//已经为自定义配置
	int ERROR_AlREADY_GENERAL_CONFIG = 10007;//已经为通用配置
    int PARAMTER_UNUSUAL = 10008;
    int ERROR_NO_PAYEE_ACCOUNT= 10009; //  未设置收款方账号
    int ERROR_REPEATE_ACCOUNT= 10010; //  重复账号
    int PARAMTER_LOSE = 10011;
    int ERROR_REFUND_ERROR = 10012;//退款失败
}
