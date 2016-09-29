package com.everhomes.wx;

public interface WeChatConstant {

	public static String WECHAT_SERVER = "wechat.server";
    public static String WECHAT_APPID = "wechat.appKey";
    public static String WECHAT_APPSECRET = "wechat.appSecret";
   
    public static String ACCESSTOKEN_GRANTTYPE = "client_credential";
    public static String JSAPI_TYPE = "jsapi";
    
    public static String GET_ACCESSTOKEN = "cgi-bin/token";
    public static String GET_MEDIA = "cgi-bin/media/get";
    public static String GET_JSAPI_TICKET = "/cgi-bin/ticket/getticket";
}
