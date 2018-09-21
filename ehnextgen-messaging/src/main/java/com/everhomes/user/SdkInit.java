package com.everhomes.user;

import com.everhomes.user.sdk.UserSdkSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SdkInit implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkInit.class);

    @Value("${sdk.user.url}")
    private String userUrl;

    @Value("${sdk.tag}")
    private String sdkTag;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Sdk init ...");
        UserSdkSetting.init(userUrl, sdkTag);
    }
}
