package com.everhomes.sms;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by xq.tian on 2017/7/11.
 */
public class SendSmsListener implements ApplicationListener<SendSmsEvent> {

    @Async
    @Override
    public void onApplicationEvent(SendSmsEvent event) {
        ((SmsCallback) event.getSource()).send();
    }
}
