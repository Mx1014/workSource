package com.everhomes.sms;

import java.util.Map;

/**
 * Created by xq.tian on 2018/1/13.
 */
public interface SmsHandlerResolver {

    String SMS_HANDLER_TYPE = "sms.handler.type";
    String RESOLVER_NAME_PREFIX = "SmsHandlerResolver-";

    SmsHandler getHandlerByName(String handlerName);

    Map<SmsHandler,String[]> resolveHandler(Integer namespaceId, String[] phoneNumbers);
}
