package com.everhomes.sms;

import java.nio.charset.Charset;

public class SmsChannelBuilder {

    public enum HttpMethod { GET, POST, PATCH, PUT, DELETE }

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset GBK = Charset.forName("GBK");

    public static SmsChannel create(boolean isSecure) {
        return new SmsChannel(isSecure);
    }
}