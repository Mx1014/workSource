package com.everhomes.rest.workReport;

public interface WorkReportNotificationTemplateCode {
	String SCOPE = "work.report.notification";
	
	int POST_WORK_REPORT_VAL_FOR_RECEIVER = 1;	//给接收人发通知：提交工作汇报

	int UPDATE_WORK_REPORT_VAL_FOR_RECEIVER = 2;   //给接收人发通知：更新工作汇报
}
