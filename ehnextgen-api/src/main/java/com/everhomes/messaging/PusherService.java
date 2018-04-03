package com.everhomes.messaging;

import java.util.Map;

import com.everhomes.cert.Cert;
import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessages;
import com.everhomes.rest.pusher.PushMessageCommand;
import com.everhomes.rest.pusher.RecentMessageCommand;
import com.everhomes.user.UserLogin;

public interface PusherService {
    public void pushMessage(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg);
    DeviceMessages getRecentMessages(RecentMessageCommand cmd);
    void createCert(Cert cert);
    void pushServiceTest(PushMessageCommand cmd);
    Map<String, Long> requestDevices(Map<String, Long> deviceMap);
    void checkAndPush(UserLogin senderLogin, UserLogin destLogin, long msgId, Message msg);
    void sendXiaomiMessage();
}
