package com.everhomes.pusher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.messaging.PusherVender;
import com.everhomes.messaging.PusherVendorData;
import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.ups.oppo.push.server.sdk.common.OppoClieckType;
import com.ups.oppo.push.server.sdk.common.TargetType;
import com.ups.oppo.push.server.sdk.exception.InvalidAuthTokenException;
import com.ups.oppo.push.server.sdk.model.OppoMessage;
import com.ups.oppo.push.server.sdk.server.OppoSender;
import com.ups.oppo.push.server.sdk.util.ResultPack;
import com.ups.oppo.push.server.sdk.vo.PushResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class PusherOppo implements PusherVender{
	
	@Autowired
	UserProvider userProvider;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PusherOppo.class);
	private String appSecret;
	private String appKey;
	private String appPkgName;
	private String masterSecret; //oppo使用masterSecret初始化
	OppoSender sender;

	public PusherOppo(String appSecret, String appPkgName, String appKey, String masterSecret) {
        this.appSecret = appSecret;
        this.appPkgName = appPkgName;
        this.appKey = appKey;
        this.masterSecret = masterSecret;
        try {
			this.sender = new OppoSender(this.appKey, this.masterSecret);
		} catch (InvalidAuthTokenException e) {
		    LOGGER.error("【PusherOppo constructure】 sender - appKey=" + appKey+" secret="+appSecret+" others: ");
			e.printStackTrace();
		}
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
		
		LOGGER.debug("=====【oppo.sendPushMessage】====== deviceToken={},senderLogin={},devMessage={},msg={}",deviceToken,senderLogin,devMessage,msg);
		
		PusherUtils.checkRouter(msg, senderLogin, destLogin);
		
        String description = devMessage.getAlert();
        if(devMessage.getAlert().length() > 20) {
            devMessage.setAlert(devMessage.getAlert().substring(0, 20));    
        }
        
        String title = devMessage.getTitle();
        if(title == null) {
            title = "zuolin";
        }
        
        //昵称
//        String senderName = userProvider.getNickNameByUid(senderLogin.getUserId());
//        LOGGER.debug("===senderName={}",senderName);

        //路由参数
        JSONObject param = new JSONObject();
        param.put("message_meta", JSON.toJSONString(msg.getMeta())); //map => json

        OppoMessage message = new OppoMessage()
        		.setTitle((StringUtils.isEmpty(devMessage.getTitle()))?"新消息":(devMessage.getTitle()))
				.setSub_title("点击查看详情")
        		.setContent(msg.getContent())
        		.setClick_action_type(OppoClieckType.ACTION.getDesc())
                .setClick_action_activity("com.everhomes.android.account.launcher")
                .setAction_parameters(param);
//        		.setTitle((StringUtils.isEmpty(senderName))?"新消息":(senderName)) //null pointer
//              .setClick_action_url("https://push.meizu.com")
        		;
        		
        String regId = deviceToken;
        ResultPack<PushResult> result;
        
        LOGGER.debug("============= OppoMessage={},regId={}",message,regId);
        
        try {
            result = sender.notificationUnicast(TargetType.REG_ID, message, regId);
            LOGGER.debug("=========【PusherOppo finish】========= result={}",result);
//            LOGGER.info("Pushing oppo status=" + result.status()
//                    + " errorCode=" + result.getErrorCode().toString()
//                    + " comment=" + result.comment()
//                    + " isSucceed=" + result.isSucceed()
//                    + " code=" + result.code()
//                    + " deviceToken=" + deviceToken);
        } catch (IOException e) {
            LOGGER.error("Pushing oppo deviceToken=" + deviceToken);
        } catch (NullPointerException e){
        	LOGGER.error("NullPointerException=",e);
        }
        
	}
	
}
