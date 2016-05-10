package com.everhomes.promotion;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.messaging.AddressMessageRoutingHandler;
import com.everhomes.server.schema.tables.EhOpPromotionActivities;

@Component
public class PromotionServiceImpl implements PromotionService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressMessageRoutingHandler.class);
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private BusBridgeProvider busBridgeProvider;
    
    @PostConstruct
    void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhOpPromotionActivities.class, null), this);
     }
    
    @Override
    public void createPromotion() {
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOpPromotionActivities.class, 5l);
        //localBus.publish("global", EhOpPromotionActivities.class.getName(), 5l);
        
        this.broadcast(DaoAction.CREATE, EhOpPromotionActivities.class, 5l);
    }
    
    private void broadcast(DaoAction action, Class<?> pojoClz, Long id) {
        LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider; 
        
        String arg3 = "" + action.getName() + "." + pojoClz.getSimpleName();
        String arg1 = arg3 + "." + id.toString();
        
        //broadcast
        localBusSubscriber.onLocalBusMessage(null, arg1, id, arg3);
    }

    @Override
    public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
        
        try {
            Long id = (Long)arg2;
            if(null == id) {
                LOGGER.error("None of groupMember");
                return Action.none;
            } else {
                LOGGER.error(" id= " + id);
                }
        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
            }

        return Action.none;
    }
}
