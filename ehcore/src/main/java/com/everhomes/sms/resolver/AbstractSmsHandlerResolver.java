package com.everhomes.sms.resolver;

import com.everhomes.sms.SmsHandler;
import com.everhomes.sms.SmsHandlerResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq.tian on 2018/1/13.
 */
abstract public class AbstractSmsHandlerResolver implements SmsHandlerResolver {

    protected final Map<String, SmsHandler> handlers = new HashMap<>();

    @Autowired
    public void setHandlers(Map<String, SmsHandler> prop) {
        prop.forEach((name, handler) -> handlers.put(name.toLowerCase(), handler));
    }

    public SmsHandler getHandlerByName(String handlerName) {
        return handlers.get(handlerName.toLowerCase());
    }
}
