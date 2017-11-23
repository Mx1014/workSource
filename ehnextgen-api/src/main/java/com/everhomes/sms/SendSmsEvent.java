package com.everhomes.sms;

import org.springframework.context.ApplicationEvent;

/**
 * Created by xq.tian on 2017/7/11.
 */
public class SendSmsEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     * @param callback the component that published the event (never {@code null})
     */
    public SendSmsEvent(SmsCallback callback) {
        super(callback);
    }
}
