package com.everhomes.sms;

import com.everhomes.util.StringHelper;

import java.util.Map;

public class RspMessage {

    private Integer code;
    private String message;
    private Map<String, String> headers;

    private Map<String, ?> meta;

    public RspMessage(String message, Integer code, Map<String, String> headers) {
        this.message = message;
        this.code = code;
        this.headers = headers;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, ?> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, ?> meta) {
        this.meta = meta;
    }

    public boolean isException() {
        return this.code == -1;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
