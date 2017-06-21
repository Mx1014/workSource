// @formatter:off
package com.everhomes.rest.organization;

public interface OrganizationServiceErrorCode {
    static final String SCOPE = "organization";
    
    static final int ERROR_NO_PRIVILEGED = 100001; //没有权限
    
    static final int ERROR_PHONE_ALREADY_EXIST = 100010;
    
    static final int ERROR_INVALID_PARAMETER = 100011;
    
    static final int ERROR_PHONE_ALREADY_APPLY = 100012;
    
    static final int ERROR_FILE_IS_EMPTY = 100100;
    
    static final int ERROR_ORG_TASK_NOT_EXIST = 100201; //任务不存在
    
    static final int ERROR_ORG_TASK_ALREADY_PROCESSED = 100201; //任务已经被处理
    
    static final int ERROR_ORG_TASK_NOT_ASSIGNED_PERSONNEL = 100202; //未分配人员，需要指定人员
    
    static final int ERROR_ORG_TASK_CANNOT_OPERATE = 100203; //不能进行此操作
    
    static final int ERROR_FILE_CONTEXT_ISNULL=200001; //文件为空
    
    static final int ERROR_COMMUNITY_EXISTS=300001; //小区已存在
    
    static final int ERROR_ENTERPRISE_CONTACT_NOT_FOUND = 10101; //人员已不在在公司通讯录
    
    static final int ERROR_PARAMETER_NOT_EXIST = 400001;
    
    static final int ERROR_OBJECT_NOT_EXIST = 400002;
    
    static final int ERROR_ORG_EXIST = 500001; //公司已存在
    
    static final int ERROR_ORG_TYPE = 500002; //公司类型错误

    static final int ERROR_CONNOT_DELETE_ADMIN = 400003;
    
    static final int ERROR_ASSIGNMENT_EXISTS=400004; //人员角色已存在
    static final int ERROR_MOBILE_NUM = 500003; //手机号错误
    static final int ERROR_ORG_NOT_EXIST = 500004; // 公司不存在

    static final int ERROR_MEMBER_STSUTS_MODIFIED= 500005; //状态已被修改
    
    static final int ERROR_VERIFY_OVER_TIME= 600002; //认证超时

	static final int ERROR_EMAIL_NOT_EXISTS = 600001; //email 错误
	
	static final int ERROR_EMAIL_REPEAT = 600003; //email 已占用
	static final int ERROR_SEND_EMAIL = 600004; //email 发邮件错误

	static final int ERROR_ORG_JOB_POSITION_EXISTS = 600005; //通用岗位已存在

    int ERROR_CONTACTNAME_ISNULL = 700000;

    int ERROR_CONTACTTOKEN_ISNULL = 700001;
    int ERROR_NO_DATA = 800000;
}
