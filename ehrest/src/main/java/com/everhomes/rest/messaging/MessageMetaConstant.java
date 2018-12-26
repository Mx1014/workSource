package com.everhomes.rest.messaging;

public interface MessageMetaConstant {
    public static final String JUMP_TYPE = "jumpType";
    public static final String JUMP_OBJECT = "jumpObj";
    public static final String META_OBJECT_TYPE = "meta-object-type";
    public static final String META_OBJECT = "meta-object";
    public static final String INCLUDE = "include";
    public static final String EXCLUDE = "exclude";
    public static final String SENDER_NAME = "sender-name";
    public static final String POPUP_FLAG = "popup-flag";
    public static final String VOICE_REMIND = "voice-remind";
    public static final String MESSAGE_SUBJECT = "message-subject";// 通知消息会有消息标题
    public static final String PERSIST_TYPE = "persist-type";// 消息持久化类型 @link com.everhomes.rest.messaging.MessagePersistType
    public static final String CLIENT_HANDLER_TYPE="clientHandlerType";//客户端消息类型
    public static final String ROUTE_URL="routeUrl"; 			 //跳转链接
    public static final String COMMUNITY_ID="communityId";		 //园区id
    public static final String ORGANIZATION_ID="organizationId";	 //企业id
    public static final String FAMILY_ID="familyId";			 //家庭id
    public static final String APP_ID="appId";				 	 //appId
    public static final String MODULE_ID="moduleId";			 //moduleId 模块id
    public static final String APP_NAME="appName";		 		 //appName 跳转某个应用名字
    public static final String NAMESPACE_ID="namespaceId";		 //域空间
    public static final String CHANNEL_TYPE="channelType";		 //系统消息类型（user/group）
    public static final String META_OBJECT_ROUTER = "meta-object-router";// 通知消息router(web)
    public static final String META_OBJECT_URL = "meta-object-url";		 // 通知消息url(web)
    public static final String SYS_MSG_ROUTER_FLAG = "sys-msg-router-flag";//系统消息标志
}
