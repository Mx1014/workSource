package com.everhomes.sms;

public class SmsChannelBuilder {
    public static SmsChannel create(boolean isSecure) {
        return new SmsChannel(isSecure);
    }
}