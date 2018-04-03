package com.everhomes.messaging;

import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.user.UserLogin;

public interface PusherVender {
    String getAppSecret();
    String getAppPkgName();
    boolean checkAppData(PusherVendorData data);
    void sendPushMessage(String deviceToken, Message msg, DeviceMessage devMessage, UserLogin senderLogin, UserLogin destLogin);
}
