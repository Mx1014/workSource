package com.everhomes.rest.organization.pm;

/**
 * 定义所有PropertyMgrServiceImpl类中没有具体定义内容的开小差报错内容
 * 5.10.0版本的工作内容，创建时间：2018年10月18日19:29:16
 * @author steve
 */
public interface PropertyErrorCode {
	
	String SCOPE = "property";

	int ERROR_COMMUNITY_NOT_EXIST = 101;//园区不存在
	int ERROR_INVALID_PARAMETER = 102;//接口参数异常
	int ERROR_NULL_PARAMETER = 103;//接口参数缺失
	int ERROR_ADDRESS_MAPPING_NOT_EXIST = 104;//房源状态信息不存在
	int ERROR_FAMILY_NOT_EXIST = 105;//家庭不存在
	int ERROR_ORGANIZATION_NOT_EXIST = 106;//机构不存在
	int ERROR_NULL_MESSAGE = 107;//消息内容为空
	int ERROR_BUILDING_NOT_EXIST = 108;//楼宇不存在
	int ERROR_ADDRESS_NOT_EXIST = 109;//房源不存在
	int ERROR_NULL_FILE = 110;//上传文件为空
	int ERROR_PARSE_FILE = 111;//解析文件失败
	int ERROR_REPEATED_BILL = 112;//账单数据重复
	int ERROR_PARSE_DATE_FORMAT = 113;//日期格式解析错误
	int ERROR_BILL_NOT_EXIST = 114;//账单不存在
	int ERROR_SEND_MESSAGE_ALLOWED = 115;//该用户不欠费，不能向其发送催缴短信
	int ERROR_VENDOR_TYPE = 116;//支付方式不支持
	int ERROR_ORDER_NOT_EXIST = 117;//订单不存在
	int ERROR_BILL_NOT_VALID = 118;//账单无效
	int ERROR_INSUFFICIENT_PRIVILEGE = 119;//用户权限不足
	int ERROR_ORGANIZATION_OWNER_BEHAVIOR_NOT_EXIST = 120;//入驻信息不存在
	int ERROR_ATTACHMENT_NOT_EXIST = 121;//附件不存在
	int ERROR_EXCEL_DATA_FORMAT = 122;//excel数据格式不正确
	int ERROR_CHARGING_ITEM_NOT_EXIST = 123;//计费项不存在
	int ERROR_CREATE_RESERVATION_FAILURE = 124;//创建预约计划失败
	

}
