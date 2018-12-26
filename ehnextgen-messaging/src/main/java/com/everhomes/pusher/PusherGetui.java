package com.everhomes.pusher;

import com.alibaba.fastjson.JSON;
import com.everhomes.messaging.PusherVender;
import com.everhomes.messaging.PusherVendorData;
import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.user.UserLogin;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PusherGetui implements PusherVender {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherGetui.class);
    private String appId;
    private String appSecret;
    private String appPkgName;
    private String appKey;
    private String masterSecret;
    IGtPush sender;

    public PusherGetui(String appId, String appSecret, String appPkgName, String appKey, String masterSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.appPkgName = appPkgName;
        this.appKey = appKey;
        this.masterSecret = masterSecret;
        sender = new IGtPush(appKey, masterSecret);
        LOGGER.debug("【PusherGetui】： appSecret={},appPkgName={},appKey={},masterSecret={},appId={}", appSecret, appPkgName, appKey, masterSecret, appId);
    }

    @Override
    public String getAppSecret() {
        return appSecret;
    }

    @Override
    public String getAppPkgName() {
        return appPkgName;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    @Override
    public boolean checkAppData(PusherVendorData data) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendPushMessage(String deviceToken, Message msg, DeviceMessage devMessage, UserLogin senderLogin,
                                UserLogin destLogin) {
        LOGGER.debug("=====【PusherGetui.sendPushMessage】====== deviceToken=" + deviceToken + " sender: " + sender);

        PusherUtils.checkRouter(msg, senderLogin, destLogin);

        SingleMessage message = new SingleMessage();
        message.setOffline(true);        // 离线有效时间，单位为毫秒，可选
        message.setPushNetWorkType(0);  // 可选，判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不不限制⽹网络环境。
        message.setOfflineExpireTime(24 * 3600 * 1000);
        TransmissionTemplate template = transmissionTemplateDemo(msg, devMessage);
        message.setData(template);


        LOGGER.debug("【Getui - SingleMessage】 message={}", message);

        Target target = new Target();
        target.setAppId(appId);
        String[] split = destLogin.getPusherIdentify().split(":");
        target.setClientId(String.valueOf(split[1]));

        LOGGER.debug("【Getui】Target={}", target);

        IPushResult ret = null;
        try {
            ret = sender.pushMessageToSingle(message, target);
            LOGGER.debug("=====finish getui once time===");
        } catch (RequestException e) {
            e.printStackTrace();
            ret = sender.pushMessageToSingle(message, target, e.getRequestId());
            LOGGER.debug("=====finish getui second  time===");
        } catch (NullPointerException e) {
            e.printStackTrace();
            LOGGER.error("【getui ex】", e);
        }
        if (ret != null) {
            LOGGER.debug("getui server response： " + ret.getResponse().toString());
        } else {
            LOGGER.error("server response exception!!");
        }
    }

    //透传推送
    private TransmissionTemplate transmissionTemplateDemo(Message msg, DeviceMessage deviceMessage) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        deviceMessage.setExtra(msg.getMeta());
        template.setTransmissionContent(JSON.toJSONString(deviceMessage));
        return template;
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");   // 设置定时展示时间
    }
}
