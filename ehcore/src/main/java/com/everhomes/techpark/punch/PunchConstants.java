package com.everhomes.techpark.punch;

public class PunchConstants {

	public static final Long MODULE_ID = 50600L;
	public static final Integer PAGE_SIZE_AT_A_TIME = 1000;
	public static final Long ONE_DAY_MS = 24 * 3600 * 1000L;
	public static final Long DEFAULT_SPLIT_TIME = 4 * 3600 * 1000L;
	public static final Long MILLISECONDGMT = 8 * 3600 * 1000L;
	
	/** module id 50600L */
	public static final Long PUNCH_MODULE_ID = 50600L;
	/** PUNCH_STATUS_SCOPE ="punch.status" */
	public static final String PUNCH_STATUS_SCOPE ="punch.status";
	/** PUNCH_DEFAULT_SCOPE ="punch.default"  */
	public static final String PUNCH_DEFAULT_SCOPE ="punch.default";
	/** PUNCH_FLOW_TITLE_SCOPE ="approval.flow"  */
	public static final String PUNCH_FLOW_SCOPE ="approval.flow";
	/** PUNCH_FLOW_TITLE_SCOPE ="approval.flow.title"  */
	public static final String PUNCH_FLOW_TITLE_SCOPE ="approval.flow.title";
	/** PUNCH_FLOW_CONTEXT_SCOPE ="approval.flow.context"  */
	public static final String PUNCH_FLOW_CONTEXT_SCOPE ="approval.flow.context";
	/** PUNCH_PUSH_SCOPE ="punch.push"  */
	public static final String PUNCH_PUSH_SCOPE ="punch.push";
	/** PUNCH_EXCEL_SCOPE ="punch.excel"  */
	public static final String PUNCH_EXCEL_SCOPE ="punch.excel";
	/** PUNCH_TOOL_URI_SCOPE ="punch.tool.uri"  */
	public static final String PUNCH_TOOL_URI_SCOPE ="punch.tool.uri";
	public static final String PUNCH_QRCODE_SUBJECT ="punch.qrcode";

	public static final String PUNCH_QRCODE_TIMEOUT = "punch.qrcode.timeout";
	public static final int PUNCH_TOOL_URI_CODE = 1;
	/** EXCEL_SCHEDULE = "schedule"  */
	public static final String EXCEL_SCHEDULE = "schedule";
	/** EXCEL_RULE = "rule"  */
	public static final String EXCEL_RULE = "rule";
	/** PUNCH_REMINDER ="1"  */
	public static final String PUNCH_REMINDER ="1";
	/** PUNCH_TIME_RULE_NAME = "timeRuleName"  */
	public static final String PUNCH_TIME_RULE_NAME = "timeRuleName";

	public static final String locale = "zh_CN";
	 /**'请假申请的时间','${beginTime}至${endTime}'*/
	 public static final int PUNCH_FLOW_REQUEST_TIME = 1;
	/**${timeRules}休息（只能按现有班次排班，否则无法识别。班次信息可以在管理后台修改）*/
	public static final int PUNCH_EXCEL_SCHEDULING_REMINDER= 1;

	public static final int SUNDAY_INT =    0b1000000;
	public static final int MONDAY_INT =    0b0100000;
	public static final int TUESDAY_INT =   0b0010000;
	public static final int WEDNESDAY_INT = 0b0001000;
	public static final int THURSDAY_INT =  0b0000100;
	public static final int FRIDAY_INT =    0b0000010;
	public static final int SATURDAY_INT =  0b0000001;
	
	public static final String STATUS_SEPARATOR="/";


	/** PUNCH_EXCEL_SCOPE ="punch.excel"  */
	public static final String PUNCH_MESSAGE_SCOPE ="punch.message";
	public static final int PUNCH_MESSAGE_ADD_GUDING = 1;
	public static final int PUNCH_MESSAGE_ADD_PAIBAN = 2;
	public static final int PUNCH_MESSAGE_UPDATE_GUDING = 3;
	public static final int PUNCH_MESSAGE_UPDATE_PAIBAN = 4;
	public static final int PUNCH_MESSAGE_TIMERULES = 5;
	public static final String PUNCH_MESSAGE_RESTTIME = "6";
	

	/** REDIS monthReport Process*/
	public static final String MONTH_REPORT_PROCESS ="MONTH.REPORT.PROCESS";

	
	/**考勤加班规则不同加班类型的提示文案初始化scope:"overtime.rule.tip.info"*/
	public static final String OVERTIME_INFO_SCOPE ="overtime.rule.tip.info";
	/**未设置打卡规则*/
	public static final String OVERTIME_INFO_NORULE ="-1";
	/**未开启*/
	public static final String OVERTIME_INFO_CLOSE ="0";
	/**需要申请和打卡，时长按打卡时间计算，但不能超过申请的时间*/
	public static final String OVERTIME_INFO_BOTH ="1";
	/**需要申请，时长按申请单时间计算*/
	public static final String OVERTIME_INFO_REQUEST ="2";
	/**不需要申请，时长按打卡时间计算*/
	public static final String OVERTIME_INFO_PUNCH ="3";
	/**工作日加班：*/
	public static final String OVERTIME_INFO_WORKDAY ="4";
	/**休息日/节假日加班：*/
	public static final String OVERTIME_INFO_HOLIDAY ="5";
	

	/** 申请统计项列表 "punch.exception.statistic"  */
	public static final String EXCEPTION_STATISTIC_SCOPE ="punch.exception.statistic";
	public static final int ASK_FOR_LEAVE_TEMPLATE_CONTENT = 1;
	public static final int GO_OUT_TEMPLATE_TITLE = 2;
	public static final int GO_OUT_TEMPLATE_CONTENT = 3;
	

	/** punchType的scope "punch.punchType"  */
	public static final String PUNCH_PUNCHTYPE_SCOPE ="punch.punchType";
	/** punchType的scope "punch.time"  */
	public static final String PUNCH_TIME_SCOPE ="punch.time";
	// 打卡提醒
	public static final String PUNCH_NOTIFICATION_SCOPE = "punch.remind";
	public static final int PUNCH_NOTIFICATION_TITLE = 1;
	public static final int PUNCH_NOTIFICATION_ON_DUTY_CONTENT = 2;
	public static final int PUNCH_NOTIFICATION_OFF_DUTY_CONTENT = 3;
	public static final int PUNCH_NOTIFICATION_DISPLAY_NAME = 4;

	public static final String NEXT_DAY ="nextDay";
	public static final String PUNCH_CREATE_TYPE ="punch.create.type";
	public static final String PUNCH_CREATE_MSG ="punch.create";
	

	/**单位 scope*/
	public static final String UNIT_SCOPE ="oa.unit";
	/**次*/
	public static final String UNIT_TIME ="2";
	/**天*/
	public static final String UNIT_DAY ="1";
}
