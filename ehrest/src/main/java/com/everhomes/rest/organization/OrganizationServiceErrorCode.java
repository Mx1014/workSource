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
   
}
