package com.everhomes.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;

/**
 * @author ycx
 */
@Component
public class PaySDKProxyImpl implements ApplicationListener<ContextRefreshedEvent>{

    private static final Logger LOGGER = LoggerFactory.getLogger(PaySDKProxyImpl.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            //init();
        }
    }
    
}
