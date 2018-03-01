package com.everhomes.rest.workReport;

public interface WorkReportNotificationTemplateCode {
	String SCOPE = "work.report.notification";
	
	int POST_WORK_REPORT_VAL = 1;	//给接收人发通知：提交工作汇报

	int UPDATE_WORK_REPORT_VAL = 2;   //给接收人发通知：更新工作汇报

	int AUTHOR_REPLY_WORK_REPORT_VAL = 3;   //给读者发通知：作者回复了你

	int AUTHOR_COMMENT_WORK_REPORT_VAL = 4;   //给接收人发通知：作者发表了评论

	int READER_REPLY_WORK_REPORT_VAL_FOR_AUTHOR   = 5;   //给作者发通知：读者回复了你

	int READER_REPLY_WORK_REPORT_VAL_FOR_READER = 6;   //给读者发通知：读者在作者的汇报中回复了你

	int READER_COMMENT_WORK_REPORT_VAL = 7;   //给作者发通知：读者发表了评论


}
