package com.everhomes.messaging;

import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.user.UserLogin;

public interface PusherVendorService {

    void stopService(String name);

    void pushMessage(PusherVenderType venderType, UserLogin senderLogin, UserLogin destLogin, com.everhomes.msgbox.Message msg, DeviceMessage devMessage);

    void pushMessageAsync(PusherVenderType venderType, UserLogin senderLogin,
            UserLogin destLogin, com.everhomes.msgbox.Message msg, DeviceMessage devMessage);
    
}
