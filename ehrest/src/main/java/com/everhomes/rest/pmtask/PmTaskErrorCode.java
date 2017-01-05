package com.everhomes.rest.pmtask;

public interface PmTaskErrorCode {

	String SCOPE = "pmtask";

	int ERROR_CATEGORY_EXIST = 10001;  //任务分类已存在
	
    int ERROR_CATEGORY_NULL = 10002;  //任务分类不存在
    
    int ERROR_USER_NULL = 10003; //目标用户不存在
    
    int ERROR_CONTENT_NULL = 10004; //内容不能为空
    
    int ERROR_SERVICE_CATEGORY_EXIST = 10005;  //服务类型已存在
    
    int ERROR_SERVICE_CATEGORY_NULL = 10006;  //服务类型不存在
    
    int ERROR_TASK_PROCCESING = 10007;  //该单已被其他人处理，请返回主界面刷新任务
    
    int ERROR_ENABLE_FLOW = 10008;  //请启用工作流
    
}
