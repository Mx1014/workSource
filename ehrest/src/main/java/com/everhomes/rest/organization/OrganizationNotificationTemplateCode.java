package com.everhomes.rest.organization;

public interface OrganizationNotificationTemplateCode {
	static final String SCOPE = "organization.notification";
	
	static final int ORGANIZATION_MEMBER_APPROVE_FOR_APPLICANT = 1;//给用户发通知：同意用户加入组织
	static final int ORGANIZATION_MEMBER_APPROVE_FOR_MANAGER = 2;//给其他管理员发通知：同意用户加入组织
	
	static final int ORGANIZATION_MEMBER_REJECT_FOR_APPLICANT = 3;//给用户发通知：拒绝用户加入组织
	static final int ORGANIZATION_MEMBER_REJECT_FOR_MANAGER = 4;//给其他管理员发通知：拒绝用户加入组织

	static final int ORGANIZATION_ASSIGN_TOPIC_FOR_COMMENT = 5;//在请求服务帖发评论:分配请求服务帖任务给处理员
	static final int ORGANIZATION_ASSIGN_TOPIC_FOR_MEMBER = 6;//给被分配人员发短信:分配请求服务帖任务给处理员
	static final int ORGANIZATION_ASSIGN_TOPIC_BY_MANAGER_FOR_MEMBER = 7;//给被分配人员发短信:分配请求服务帖任务给处理员,用户求助由管理员代发
	
	static final int ORGANIZATION_MEMBER_DELETE_FOR_MANAGER = 8;//通知其他管理员：删除组织成员
	
	static final int ORGANIZATION_TASK_ACCEPT_COMMENT = 10;// 接受任务回复的消息
	
	static final int ORGANIZATION_TASK_NEW = 11;// 新发布一条任务短信消息
	
	static final int ORGANIZATION_TASK_PROCESSING = 12;// 处理任务时发送的短信消息
	
	static final int ORGANIZATION_TASK_PROCESSING_COMMENT = 13;// 处理任务时回复的帖子消息
	
	static final int ORGANIZATION_TASK_REFUSE = 14;// 任务被拒绝收到的短信消息
	
	static final int ORGANIZATION_TASK_FINISH = 15;// 任务已完成后的短信消息
	
	static final int ORGANIZATION_TASK_FINISH_COMMENT = 16;// 任务已完成后回复的帖子消息
	
	static final int ORGANIZATION_APPLY_FOR_CONTACT = 17;// 有用户申请认证公司时给公司管理员发消息
	
}
