package com.everhomes.messaging;

import com.everhomes.rest.messaging.DeviceMessage;

public interface PusherVender {
    String getAppSecret();
    String getAppPkgName();
    boolean checkAppData(PusherVendorData data);
    void sendPushMessage(String deviceToken, com.everhomes.msgbox.Message msg, DeviceMessage devMessage);
}
