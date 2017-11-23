// @formatter:off
package com.everhomes.bus;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BusBridgeProviderImpl implements BusBridgeProvider, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusBridgeProviderImpl.class);

    @Value("${redis.bus.host}")
    private String redisServerHost;
    
    @Value("${redis.bus.port}")
    private int redisServerPort;
    
    @Autowired
    private LocalBus localBusProvider;
    
    @Autowired
    private CoreBusProvider coreBusProvider;
    
    private String bridgeUuid = UUID.randomUUID().toString();

    @PostConstruct
    public void setup() {
        localBusProvider.subscribe("global", this);
        
        coreBusProvider.setup(redisServerHost, redisServerPort, new CoreBusReceiver(), null);
    }
    
    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        BusBridgeMessage bridgeMessage = new BusBridgeMessage();
        bridgeMessage.setInitiator(this.bridgeUuid);
        bridgeMessage.setSubject(subject);
        bridgeMessage.setArgs(args);
        
        this.coreBusProvider.sendRawBusMessage(bridgeMessage);
        return Action.none;
    }
    
    public void onCoreBusMessage(Object message) {
        if(message != null && message instanceof BusBridgeMessage) {
            BusBridgeMessage bridgeMesssage = (BusBridgeMessage)message;
            //this 实现了LocalBusSubscriber 订阅了 global 主题，避免死循环
            if(!bridgeUuid.equals(bridgeMesssage.getInitiator())) {
                localBusProvider.publish(null, bridgeMesssage.getSubject(), bridgeMesssage.getArgs());
            } else {
                LOGGER.trace("Drop bridge message from core bus since it is originated from itself");
            }
        }
    }
    
    public class CoreBusReceiver {
        public void handleMessage(Object message) {
            onCoreBusMessage(message);
        }
    }
}

