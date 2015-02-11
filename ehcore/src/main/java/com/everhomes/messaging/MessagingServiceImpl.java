// @formatter:off
package com.everhomes.messaging;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.everhomes.user.UserLogin;import com.everhomes.util.Name;


/**
 * Implements MessagingService
 * 
 * @author Kelven Yang
 *
 */
@Component
public class MessagingServiceImpl implements MessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingServiceImpl.class);

    @Autowired
    private List<MessageRoutingHandler> handlers;
    
    private Map<String, MessageRoutingHandler> handlerMap = new ConcurrentHashMap<>();

    public MessagingServiceImpl() {
    }
    
    @PostConstruct
    public void setup() {
        if(handlers != null) {
            handlers.stream().forEach((handler) -> {
                Name name = handler.getClass().getAnnotation(Name.class);
                if(name != null && name.value() != null) {
                    registerRoutingHandler(name.value(), handler);
                } else {
                    LOGGER.error("MessageRoutingHandler " + handler.getClass().getName() + " is not properly annotated with @Name");
                }
            });
        }
    }
    
    @Override
    public void routeMessage(UserLogin senderLogin, long appId, String dstChannelType,
            String dstChannelToken, Map<String, String> messageMeta,
            String messageBody, int deliveryOption) {
        MessageRoutingHandler handler = handlerMap.get(dstChannelType);
        if(handler != null) {
            if(handler.allowToRoute(senderLogin, appId, dstChannelType, dstChannelToken, messageMeta, messageBody)) {
                handler.routeMessage(senderLogin, appId, dstChannelType, dstChannelToken, messageMeta, messageBody, deliveryOption);
            } else {
                if(LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("Message to %s:%s is dropped due to filtering", dstChannelType, dstChannelToken));  
            }
        } else {
            LOGGER.error(String.format("Unable to route message %s:%s", dstChannelType, dstChannelToken));
        }
    }

    @Override
    public void registerRoutingHandler(String channelType,
            MessageRoutingHandler handler) {
        assert(channelType != null);
        
        handlerMap.put(channelType, handler);
    }

    @Override
    public void unregisterRoutingHandler(String channelType) {
        assert(channelType != null);
        
        handlerMap.remove(channelType);
    }
}
