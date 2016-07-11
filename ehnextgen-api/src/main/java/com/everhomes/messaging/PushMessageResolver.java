package com.everhomes.messaging;

import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.DeviceMessage;
import com.everhomes.user.UserLogin;

public interface PushMessageResolver {
    public static final String PUSH_MESSAGE_RESOLVER_PREFIX = "pusherApp-";
    public static final String PUSH_MESSAGE_RESOLVER_DEFAULT = "pusherApp-1";
    
    DeviceMessage resolvMessage(final UserLogin senderLogin, final UserLogin destLogin, final Message msg);
}
