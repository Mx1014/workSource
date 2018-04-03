package com.everhomes.pusher;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.everhomes.messaging.ApnsServiceFactory;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PusherAction implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherAction.class);
    
    @Autowired
    ApnsServiceFactory apnsServiceFactory;
    
    private final String payload;
    private final String identify;
    private final String partner;
    
    @Override
    public void run() {
        ApnsService tempService = apnsServiceFactory.getApnsService(this.partner);
        if(null == tempService) {
            LOGGER.error("Pushing message, the cert for namespace not found, namespaceId=" + partner + ", deviceId=" + identify);
            return;
         }
        
        int now =  (int)(new Date().getTime()/1000);
        
        EnhancedApnsNotification notification = new EnhancedApnsNotification(EnhancedApnsNotification.INCREMENT_ID() /* Next ID */,
                    now + 60 * 60 /* Expire in one hour */,
                    identify /* Device Token */,
                    payload);
        try {
            tempService.push(notification);   
        } catch (NetworkIOException e) {
            apnsServiceFactory.stopApnsServiceByName(this.partner);
        }
        
     
//        LOGGER.info("build: pushing to: " + identify);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pushing message(ios msg flush), namespaceId=" + partner + ", deviceId=" + identify);
        }
        Map<String, Date> inactiveDevices = tempService.getInactiveDevices();
//        for (String deviceToken : inactiveDevices.keySet()) {
//            Date inactiveAsOf = inactiveDevices.get(deviceToken);
//            LOGGER.error("inactiveAsOf: " + inactiveAsOf.toString() + " deviceToken: " + deviceToken);
//          }
        if(inactiveDevices != null && inactiveDevices.size() > 0 && LOGGER.isInfoEnabled()) {
            LOGGER.info("Pushing message, device is inactive, namespaceId=" + partner + ", deviceId=" + identify 
                + ", inactiveDevices=" + inactiveDevices);
        }
    }

    public PusherAction(final String payload, final String identify, final String partner) {
        this.payload = payload;
        this.identify = identify;
        this.partner = partner;
    }
}
