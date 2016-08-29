package com.everhomes.rest.pmtask;

public interface PmTaskErrorCode {

	String SCOPE = "pmtask";

	int ERROR_CATEGORY_EXIST = 10001;  //任务分类已存在
	
    int ERROR_CATEGORY_NULL = 10002;  //服务类型不存在
    
    int ERROR_PLATE_APPLIED = 10003;  //车牌已申请
    int ERROR_PLATE_NULL = 10005;  //车牌号为空
    
    int ERROR_PLATE_APPLIED_SERVER = 10004;  //服务器忙
}
