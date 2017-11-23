package com.everhomes.pusher;

import java.io.IOException;

import com.everhomes.msgbox.Message;
import com.everhomes.user.UserLogin;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.messaging.PusherVender;
import com.everhomes.messaging.PusherVendorData;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.util.StringHelper;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class PusherXiaomi implements PusherVender {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherXiaomi.class);
    private String appSecret;
    private String appPkgName;
    Sender sender;
    
    public PusherXiaomi(String appSecret, String appPkgName) {
        this.appSecret = appSecret;
        this.appPkgName = appPkgName;
        this.sender = new Sender(this.appSecret);
    }
    
    public String getAppId() {
        return "";
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public String getAppPkgName() {
        return this.appPkgName;
    }
    
    @Override
    public void sendPushMessage(String deviceToken, Message msgBox, DeviceMessage devMessage, UserLogin senderLogin, UserLogin destLogin) {
        String description = devMessage.getAlert();
        if(devMessage.getAlert().length() > 20) {
            devMessage.setAlert(devMessage.getAlert().substring(0, 20));    
        }
        
        String messagePayload = StringHelper.toJsonString(devMessage);
        String title = devMessage.getTitle();
        if(title == null) {
            title = "zuolin";
        }
        com.xiaomi.xmpush.server.Message message = new com.xiaomi.xmpush.server.Message.Builder()
            .title(title)
            .description(description).payload(messagePayload)
            .restrictedPackageName(this.appPkgName)
            .passThrough(0)
            .notifyType(1)     // 使用默认提示音提示
            .build();
        String regId = deviceToken;
        Result result;
        try {
            result = sender.send(message, regId, 3);
            LOGGER.info("Pushing xiaomi messageId=" + result.getMessageId()
                    + " errorCode=" + result.getErrorCode().toString()
                    + " reason=" + result.getReason() + " deviceToken=" + deviceToken);
        } catch (IOException | ParseException e) {
            LOGGER.error("Pushing xiaomi deviceToken=" + deviceToken);
        }
    }

    @Override
    public boolean checkAppData(PusherVendorData data) {
        // TODO Auto-generated method stub
        return false;
    }

}
