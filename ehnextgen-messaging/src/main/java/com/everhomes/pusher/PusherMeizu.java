package com.everhomes.pusher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.messaging.PusherVender;
import com.everhomes.messaging.PusherVendorData;
import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;
import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PusherMeizu implements PusherVender{
	
	@Autowired
	UserProvider userProvider;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PusherMeizu.class);
	private String appId;
	private String appSecret;
	private String appPkgName;
	IFlymePush sender;
	
	public PusherMeizu(String appId, String appSecret, String appPkgName) {
		this.appId = appId;
	    this.appSecret = appSecret;
        this.appPkgName = appPkgName;
        this.sender = new IFlymePush(this.appSecret);
        LOGGER.debug("【PusherMeizu】： appSecret= "+appSecret+" appPkgName= "+appPkgName+" appId="+appId);
	}

	@Override
	public String getAppSecret() {
		return appSecret;
	}

	@Override
	public String getAppPkgName() {
		return this.appPkgName;
	}

	@Override
	public boolean checkAppData(PusherVendorData data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendPushMessage(String deviceToken, Message msg, DeviceMessage devMessage, UserLogin senderLogin,
			UserLogin destLogin) {
		
		LOGGER.debug("=====【meizu.sendPushMessage】====== deviceToken={},senderLogin={},devMessage={},msg={}",deviceToken,senderLogin,devMessage,msg);
		
		PusherUtils.checkRouter(msg, senderLogin, destLogin);

		//昵称
//		 String senderName = userProvider.getNickNameByUid(senderLogin.getUserId());
//		 LOGGER.debug("===senderName={}",senderName);

		//路由参数
		JSONObject param = new JSONObject();
		param.put("message_meta", JSON.toJSONString(msg.getMeta())); //map => json

        //设置返回参数
        devMessage.getExtra().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        devMessage.getExtra().put(MessageMetaConstant.META_OBJECT, msg.getMeta().get(MessageMetaConstant.META_OBJECT));

        //组装消息
        VarnishedMessage message = new VarnishedMessage.Builder()
        		.appId(Long.valueOf(appId))
                .title((StringUtils.isEmpty(devMessage.getTitle()))?"新消息":(devMessage.getTitle()))
                .content(devMessage.getAlert())
                .noticeExpandType(0)
                .noticeExpandContent(devMessage.getAlert())
                .clickType(3)
				.parameters(param)
                .offLine(true)
                .validTime(12)
                .suspend(true)
                .clearNoticeBar(true)
                .vibrate(false)
                .lights(false)
                .sound(false)
                .fixSpeed(true)
                .fixSpeedRate(20)
                .customAttribute(JSON.toJSONString(devMessage))
//                .title((StringUtils.isEmpty(senderName))?"新消息":(senderName))
//                .url("http://www.baidu.com")
                .build();


        message.setAppId(Long.valueOf(appId));
    	LOGGER.debug("【Meizu - VarnishedMessage】 message={}"+message);
		
        ResultPack<PushResult> result = null;
		try {
			List<String> pushIds = new ArrayList<String>(10);
			String[] split = destLogin.getPusherIdentify().split(":");
			pushIds.add(String.valueOf(split[1]));
			result = sender.pushMessage(message, pushIds,1);	 //失败则重试一次
			LOGGER.debug("meizu result1=",result);
//			LOGGER.debug("Pushing meizu status=" + result.status()
//			+ " errorCode=" + result.getErrorCode().toString()
//			+ " comment=" + result.comment()
//			+ " isSucceed=" + result.isSucceed()
//			+ " code=" + result.code()
//			+ " deviceToken=" + deviceToken);
		} catch (IOException e) {
			LOGGER.error("【meizu IOException】", e);
		} catch(NullPointerException e) {
			LOGGER.error("【meizu NullPointerException】", e);
		}
	}

}
