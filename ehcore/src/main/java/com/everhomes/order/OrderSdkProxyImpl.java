package com.everhomes.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.gorder.sdk.order.OrderService;
import com.everhomes.rest.gorder.http.RestClientSettings;

@Component
public class OrderSdkProxyImpl implements ApplicationListener<ContextRefreshedEvent>{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSdkProxyImpl.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrderService orderService;
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
        	String appKey = configurationProvider.getValue(0,"gorder.server.app_key", "");
        	String secretKey = configurationProvider.getValue(0,"gorder.server.app_secret", "");
        	String connectUrl = configurationProvider.getValue(0,"gorder.server.home_url", "");
        	RestClientSettings setting = new RestClientSettings(appKey, secretKey, connectUrl);
        	orderService.init(setting);
        }
    }
}
