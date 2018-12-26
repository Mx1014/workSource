package com.everhomes.pusher;

//路由参数常量 ,add by momoubin,18/12/12
public interface RouterParamsConstant {
	static final String MSG_SESSION_PREFIX="zl://message/open-session?"; //默认消息跳转的统一路由是跳转到会话
	static final String MSG_SYSTEM_PREFIX="zl://message/notice-list?";   //没有路由的系统通知
	static final String DST_CHANNEL="dstChannel=";
	static final String DST_CHANNEL_ID="dstChannelId=";
	static final String SENDER_UID="senderUid=";
	static final String CLIENT_HANDLER_TYPE="clientHandlerType=";
	
}
