// @formatter:off
package com.everhomes.sms;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Component
public class SendSmsListener implements ApplicationListener<SendSmsEvent> {

    // private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsListener.class);

    @Async
    @Override
    public void onApplicationEvent(SendSmsEvent event) {
        ((SmsCallback) event.getSource()).send();
    }
}
