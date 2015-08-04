package com.everhomes.organization;

public interface OrganizationNotificationTemplateCode {
	static final String SCOPE = "organization.notification";
	
	static final int ORGANIZATION_MEMBER_APPROVE_FOR_APPLICANT = 1;//给用户发通知：同意用户加入组织
	static final int ORGANIZATION_MEMBER_APPROVE_FOR_MANAGER = 2;//给其他管理员发通知：同意用户加入组织
	
	static final int ORGANIZATION_MEMBER_REJECT_FOR_APPLICANT = 3;//给用户发通知：拒绝用户加入组织
	static final int ORGANIZATION_MEMBER_REJECT_FOR_MANAGER = 4;//给其他管理员发通知：拒绝用户加入组织

	static final int ORGANIZATION_ASSIGN_TOPIC_FOR_COMMENT = 5;//在请求服务帖发评论:分配请求服务帖任务给处理员
	static final int ORGANIZATION_ASSIGN_TOPIC_FOR_MEMBER = 6;//给被分配人员发短信:分配请求服务帖任务给处理员
	static final int ORGANIZATION_ASSIGN_TOPIC_BY_MANAGER_FOR_MEMBER = 7;//给被分配人员发短信:分配请求服务帖任务给处理员,用户求助由管理员代发
	
	

}
