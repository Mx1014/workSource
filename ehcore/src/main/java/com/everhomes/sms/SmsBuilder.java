package com.everhomes.sms;

import java.util.HashMap;
import java.util.Map;

public class SmsBuilder {
    private final static SmsBuilder builder = new SmsBuilder();
    private final Map<Boolean, SmsChannel> cache;

    public enum HttpMethod {
        POST("POST"), PATCH("PATCH"), GET("GET"), DELETE("DELETE");
        private String method;

        HttpMethod(String method) {
            this.method = method;
        }

        public String val() {
            return this.method;
        }
    }

    private SmsBuilder() {
        cache = new HashMap<>();
        cache.put(true, new SmsChannel(true));
        cache.put(false, new SmsChannel(false));
    }

    public SmsChannel getChannel(boolean isSecure) {
        return cache.get(isSecure);
    }

    public static SmsChannel create(boolean isSecure) {
        return builder.getChannel(isSecure);
    }

}
