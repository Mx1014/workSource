package com.everhomes.rest.oauth2client;

/**
 * Created by xq.tian on 2017/3/13.
 */
public interface OAuth2ClientServiceErrorCode {

    String SCOPE = "oauth2client";

    int ERROR_OAUTH2_SERVER_NOT_EXISTS = 10000;// 没有找到oauth2 server
    int ERROR_OAUTH2_SERVER_RESPONSE_ERROR = 10001;// oauth2 server 响应错误
    int ERROR_SERVICE_URL_NOT_FOUND_ERROR = 10002;// 没有找到对应的业务跳转地址
}
