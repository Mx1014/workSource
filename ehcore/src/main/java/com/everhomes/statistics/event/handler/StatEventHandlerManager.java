package com.everhomes.statistics.event.handler;

import com.everhomes.statistics.event.StatEventHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/11/16.
 */
@Component
public class StatEventHandlerManager implements InitializingBean {

    @Autowired
    private List<StatEventHandler> handlers;

    private static final Map<String, StatEventHandler> handlerMap = new HashMap<>();

    public static StatEventHandler getHandler(String eventName) {
        return handlerMap.get(eventName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerInit();
    }

    private void handlerInit() {
        if (handlers != null) {
            for (StatEventHandler handler : handlers) {
                handlerMap.put(handler.getEventName(), handler);
            }
        }
    }
}
