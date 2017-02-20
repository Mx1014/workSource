package com.everhomes.rest.business;

public class BusinessServiceErrorCode {
    public static final String SCOPE = "business";
    
    public static final int ERROR_BUSINESS_NOT_EXIST = 10001; //商家不存在
    public static final int ERROR_BIZ_API_NOT_EXIST = 10002; //电商api没有拿到
    public static final int ERROR_BIZ_SERVER_RESPONSE = 10003; //电商服务器响应错误
}
