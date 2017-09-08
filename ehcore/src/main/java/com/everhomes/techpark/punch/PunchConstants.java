package com.everhomes.techpark.punch;

public class PunchConstants {
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

}
