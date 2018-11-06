package com.everhomes.rest.workReport;

public interface WorkReportNotificationTemplateCode {

	String SCOPE = "work.report.notification";

	int COMMENT_MESSAGE_1 = 1; //	${评论人} 在Ta的 ${汇报类型}（${汇报日期}） 中 回复了你

	int COMMENT_MESSAGE_2 = 2; //	${评论人} 在Ta的 ${汇报类型} （${汇报日期}）中 发表了评论

	int COMMENT_MESSAGE_3 = 3; //	${评论人} 在你的 ${汇报类型} （${汇报日期}）中 回复了你

	int COMMENT_MESSAGE_4 = 4; //	${评论人} 在${提交人}的 ${汇报类型} （${汇报日期}）中 回复了你

	int COMMENT_MESSAGE_5 = 5; //	${评论人} 在你的 ${汇报类型}（${汇报日期}） 中 发表了评论


	int POST_WORK_REPORT_VAL = 101;	//给接收人发通知：提交工作汇报

	int UPDATE_WORK_REPORT_VAL = 102;   //给接收人发通知：更新工作汇报
}
