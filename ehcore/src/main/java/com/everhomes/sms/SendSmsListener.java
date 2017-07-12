// @formatter:off
package com.everhomes.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Component
public class SendSmsListener implements ApplicationListener<SendSmsEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsListener.class);

    @Async
    @Override
    public void onApplicationEvent(SendSmsEvent event) {
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug("sms listener listened a sms event...");
        // }
        ((SmsCallback) event.getSource()).send();
    }
}
