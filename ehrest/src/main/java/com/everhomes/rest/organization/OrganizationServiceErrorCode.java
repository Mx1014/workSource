// @formatter:off
package com.everhomes.rest.organization;

public interface OrganizationServiceErrorCode {
    static final String SCOPE = "organization";
    
    static final int ERROR_PHONE_ALREADY_EXIST = 100010;
    
    static final int ERROR_INVALID_PARAMETER = 100011;
    
    static final int ERROR_PHONE_ALREADY_APPLY = 100012;
    
    static final int ERROR_FILE_IS_EMPTY = 100100;
    
    static final int ERROR_ORG_TASK_NOT_EXIST = 100201; //任务不存在
    
    static final int ERROR_ORG_TASK_ALREADY_PROCESSED = 100201; //任务已经被处理
    
    static final int ERROR_ORG_TASK_NOT_ASSIGNED_PERSONNEL = 100201; //未分配人员，需要指定人员
    
    static final int ERROR_ORG_TASK_CANNOT_OPERATE = 100201; //不能进行此操作
   
}
