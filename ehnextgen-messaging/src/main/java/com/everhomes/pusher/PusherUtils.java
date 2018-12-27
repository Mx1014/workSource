package com.everhomes.pusher;

import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.user.UserLogin;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PusherUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PusherUtils.class);

	/**
	 * meta路由检测：系统消息路由 or 会话消息路由
	 * */
	public static void checkRouter(Message msg, UserLogin senderLogin, UserLogin destLogin){
		if(StringUtils.isEmpty(msg.getMeta().get(MessageMetaConstant.META_OBJECT)) &&
				StringUtils.isEmpty(MetaObjectType.fromCode(msg.getMeta().get(MessageMetaConstant.META_OBJECT_TYPE)))){
			LOGGER.debug("========= Message's META_OBJECT or META_OBJECT_TYPE is null ========= msg={}",msg);
			
			//系统消息路由 or 会话消息路由
			String router = "";
			if((!StringUtils.isEmpty(msg.getMeta().get(MessageMetaConstant.SYS_MSG_ROUTER_FLAG))
					&& msg.getMeta().get(MessageMetaConstant.SYS_MSG_ROUTER_FLAG).equals(MessageMetaConstant.SYS_MSG_ROUTER_FLAG))
					){
					router = getRouter(RouterParamsConstant.MSG_SYSTEM_PREFIX,msg,senderLogin,destLogin); //系统消息路由
			}else if(StringUtils.isEmpty(msg.getMeta().get(MessageMetaConstant.META_OBJECT))){
				router = getRouter(RouterParamsConstant.MSG_SESSION_PREFIX,msg,senderLogin,destLogin);//会话消息路由
			}else{
				LOGGER.debug("====== router is normal======");
				return;
			}

			
			RouterMetaObject metaObject = new RouterMetaObject();
//			metaObject.setRouter(router); //等ios和Android统一步伐后再说，暂时只设置url
			metaObject.setUrl(router);
			msg.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
			msg.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
			LOGGER.debug("final: msg={},router={}",msg,router);
			
		}else{
			LOGGER.debug("checkRouter: msg={},senderLogin={},destLogin={}",msg,senderLogin,destLogin);
			LOGGER.debug("=========【checkRouter finish】=======");
		}
	}
	
	public static String getRouter(String prefix, Message msg, UserLogin senderLogin, UserLogin destLogin){
		StringBuffer router = new StringBuffer();
		router.append(prefix);
		router.append(RouterParamsConstant.DST_CHANNEL).append(msg.getChannelType()).append("&");
		router.append(RouterParamsConstant.DST_CHANNEL_ID).append(msg.getChannelToken()).append("&");
		router.append(RouterParamsConstant.SENDER_UID).append(String.valueOf(senderLogin.getUserId()));
//		router.append(RouterParamsConstant.SENDER_UID).append(String.valueOf(senderLogin.getUserId())).append("&");
//		router.append(RouterParamsConstant.CLIENT_HANDLER_TYPE).append(String.valueOf(0)); //普通路由不加该字段
		return router.toString();
	}
	
}
