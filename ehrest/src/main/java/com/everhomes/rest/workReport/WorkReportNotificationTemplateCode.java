package com.everhomes.rest.workReport;

public interface WorkReportNotificationTemplateCode {
	String SCOPE = "work.report.notification";
	
	int POST_WORK_REPORT_VAL = 1;	//给接收人发通知：提交工作汇报

	int UPDATE_WORK_REPORT_VAL = 2;   //给接收人发通知：更新工作汇报

	int READER_COMMENT_WORK_REPORT_VAL = 3;   //给作者发通知：读者发表了评论

	int AUTHOR_REPLY_WORK_REPORT_VAL = 4;   //给读者发通知：作者发表了回复

	int READER_WORK_REPORT_VAL_FOR_READER = 5;   //给读者发通知：读者在作者的汇报中发表了回复

	int AUTHOR_COMMENT_WORK_REPORT_VAL   = 6;   //给接收人发通知：作者发表了评论
}
