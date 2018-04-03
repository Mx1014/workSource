package com.everhomes.rest.approval;

public interface ApprovalNotificationTemplateCode {
	String SCOPE = "approval.notification";
	
	int ABSENCE_COMMIT_REQUEST = 11; // ${creatorName}提交了请假申请，请假时间：${time}，请及时到后台进行处理。
	int ABSENCE_TO_NEXT_LEVEL = 12; // ${creatorName}提交了请假申请，请假时间：${time}，${approver}已同意，请及时到后台进行处理。
	int ABSENCE_APPROVED = 13; // 您提交的请假申请已通过审批，请假时间：${time}。
	int ABSENCE_REJECTED = 14; // 您提交的请假申请被${approver}驳回，理由是：${reason}，请假时间：${time}。
	
	int EXCEPTION_COMMIT_REQUEST = 21; // ${creatorName}提交了异常申请，请及时到后台进行处理。
	int EXCEPTION_TO_NEXT_LEVEL = 22; // ${creatorName}提交了异常申请，${approver}已同意，请及时到后台进行处理。
	int EXCEPTION_APPROVED = 23; // 您对${punchDate}提交的异常申请已通过审批。
	int EXCEPTION_REJECTED = 24; // 您对${punchDate}提交的异常申请被${approver}驳回，理由是：${reason}。


	int OVERTIME_COMMIT_REQUEST = 31; //${creatorName}提交了加班申请，申请${date}加班${hour}小时，请及时进行处理。
	int OVERTIME_TO_NEXT_LEVEL = 32; //${creatorName}提交了加班申请，申请${date}加班${hour}小时，${approver}已同意，请及时进行处理。
	int OVERTIME_APPROVED = 33; //您的加班申请：${date}加班${hour}小时，已通过审批。
	int OVERTIME_REJECTED = 34; //您的加班申请：${date}加班${hour}小时，被${approver}驳回，理由是：${reason}。
	

	
}
