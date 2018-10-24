package com.everhomes.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.user.UserContext;

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
        	String appKey = configurationProvider.getValue(0,"pay.v2.appKey", "");
        	String secretKey = configurationProvider.getValue(0,"pay.v2.secretKey", "");
        	String payHomeUrl = configurationProvider.getValue(0,"pay.v2.payHomeUrl", "");
        	String localServerUrl = configurationProvider.getValue(0,"home.url", "") + "/evh";
        	PaySettings.init(appKey, secretKey, payHomeUrl, localServerUrl);
        	if(LOGGER.isDebugEnabled()) {
        	    LOGGER.debug("Init the http connection config in payserver, appKey={}, payHomeUrl={}, localServerUrl={}", appKey, payHomeUrl, localServerUrl);
        	}
        }
    }
}
