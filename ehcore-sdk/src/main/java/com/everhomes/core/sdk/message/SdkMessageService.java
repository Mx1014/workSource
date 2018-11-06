package com.everhomes.core.sdk.message;

import com.everhomes.core.sdk.NsDispatcher;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.admin.SendMessageAdminCommand;
import com.everhomes.rest.messaging.admin.SendMessageAdminTargetType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SdkMessageService extends NsDispatcher {

    public void sendMessage(Integer namespaceId, Long targetUid, String body, Map<String, String> meta) {
        SendMessageAdminCommand cmd = new SendMessageAdminCommand();
        cmd.setBody(body);
        cmd.setTargetToken(targetUid);
        cmd.setTargetType(SendMessageAdminTargetType.USER.getCode());
        cmd.setBodyType(MessageBodyType.TEXT.getCode());
        cmd.setDeliveryOption(MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        cmd.setMeta(meta);

        dispatcher(namespaceId, (sdkClient) -> {
            return sdkClient.restCall("post", "/evh/admin/message/send", cmd, RestResponse.class);
        });
    }
}