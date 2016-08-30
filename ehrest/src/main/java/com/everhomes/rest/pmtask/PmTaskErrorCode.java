package com.everhomes.rest.pmtask;

public interface PmTaskErrorCode {

	String SCOPE = "pmtask";

	int ERROR_CATEGORY_EXIST = 10001;  //任务分类已存在
	
    int ERROR_CATEGORY_NULL = 10002;  //服务类型不存在
    
    int ERROR_USER_NULL = 10003; //目标用户不存在
}
