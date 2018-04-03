package com.everhomes.rest.community;

public interface ResourceCategoryErrorCode {

	String SCOPE = "resource.category";

    int ERROR_RESOURCE_CATEGORY_LENGTH = 10002;  //分类名称不能超过10个字
    
    int ERROR_RESOURCE_CATEGORY_EXIST = 10003;  //分类名称已存在
    
    int ERROR_RESOURCE_CATEGORY_NULL = 10004;  //分类不存在
}
