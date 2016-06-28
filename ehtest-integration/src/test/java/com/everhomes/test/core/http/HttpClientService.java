package com.everhomes.test.core.http;

import java.io.File;
import java.util.Map;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.test.core.base.UserContext;

public interface HttpClientService {
    /**
     * 发HTTP POST请求（用于非登录用户的场合）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    <T extends RestResponseBase> T restPost(String commandRelativeUri, Object cmd, Class<T> responseClz);
    
    /**
     * 发HTTP GET请求（用于非登录用户的场合）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    <T extends RestResponseBase> T restGet(String commandRelativeUri, Object cmd, Class<T> responseClz);
    
    /**
     * 发HTTP POST请求（含登录用户token、userAgent等信息的上下文）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    <T extends RestResponseBase> T restPost(String commandRelativeUri, Object cmd, Class<T> responseClz, UserContext context);
    
    /**
     * 发HTTP GET请求（含登录用户token、userAgent等信息的上下文）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    public <T extends RestResponseBase> T restGet(String commandRelativeUri, Object cmd, Class<T> responseClz, UserContext context);
    
    <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Object cmd, 
        Class<T> responseClz, UserContext context);
    
    <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Map<String, String> params, 
        Class<T> responseClz, UserContext context);
    
    boolean isReponseSuccess(RestResponseBase response);

    String getServerContextPath();

    void setServerContextPath(String serverContextPath);

    String getServerAddress();

    void setServerAddress(String serverAddress);

    <T extends RestResponseBase> T postFile(String commandRelativeUri, Object cmd, File file, Class<T> responseClz);
}
