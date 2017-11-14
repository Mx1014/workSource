package com.everhomes.rest.organization;

public interface OrganizationNotificationTemplateCode {
	String SCOPE = "organization.notification";
	
	int ORGANIZATION_MEMBER_APPROVE_FOR_APPLICANT = 1;//给用户发通知：同意用户加入组织
	int ORGANIZATION_MEMBER_APPROVE_FOR_MANAGER = 2;//给其他管理员发通知：同意用户加入组织
	
	int ORGANIZATION_MEMBER_REJECT_FOR_APPLICANT = 3;//给用户发通知：拒绝用户加入组织
	int ORGANIZATION_MEMBER_REJECT_FOR_MANAGER = 4;//给其他管理员发通知：拒绝用户加入组织

	int ORGANIZATION_ASSIGN_TOPIC_FOR_COMMENT = 5;//在请求服务帖发评论:分配请求服务帖任务给处理员
	int ORGANIZATION_ASSIGN_TOPIC_FOR_MEMBER = 6;//给被分配人员发短信:分配请求服务帖任务给处理员
	int ORGANIZATION_ASSIGN_TOPIC_BY_MANAGER_FOR_MEMBER = 7;//给被分配人员发短信:分配请求服务帖任务给处理员,用户求助由管理员代发
	
	int ORGANIZATION_MEMBER_DELETE_FOR_MANAGER = 8;//通知其他管理员：删除组织成员
	
	int ORGANIZATION_TASK_ACCEPT_COMMENT = 10;// 接受任务回复的消息
	
	int ORGANIZATION_TASK_NEW = 11;// 新发布一条任务短信消息
	
	int ORGANIZATION_TASK_PROCESSING = 12;// 处理任务时发送的短信消息
	
	int ORGANIZATION_TASK_PROCESSING_COMMENT = 13;// 处理任务时回复的帖子消息
	
	int ORGANIZATION_TASK_REFUSE = 14;// 任务被拒绝收到的短信消息
	
	int ORGANIZATION_TASK_FINISH = 15;// 任务已完成后的短信消息
	
	int ORGANIZATION_TASK_FINISH_COMMENT = 16;// 任务已完成后回复的帖子消息
	
	int ORGANIZATION_APPLY_FOR_CONTACT = 17;// 有用户申请认证公司时给公司管理员发消息

	int CREATE_ORGANIZATION_ADMIN_MESSAGE_TO_OTHER_TEMPLATE = 18;//${userName}（${contactToken}）已被添加为${organizationName}的企业管理员。
	int CREATE_ORGANIZATION_ADMIN_MESSAGE_TO_TARGET_TEMPLATE = 19;//您已被添加为${organizationName}的企业管理员。

	int DELETE_ORGANIZATION_ADMIN_MESSAGE_TO_OTHER_TEMPLATE = 20;//${userName}（${contactToken}）的${organizationName}企业管理员身份已被移除。
	int DELETE_ORGANIZATION_ADMIN_MESSAGE_TO_TARGET_TEMPLATE = 21;//您的${organizationName}企业管理员身份已被移除。

}
