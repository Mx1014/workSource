package com.everhomes.sms;

import org.apache.commons.collections.MultiMap;

public class RspMessage {

    private String message;
    private Integer code;
    private MultiMap headers;

    public RspMessage(String message, Integer code, MultiMap headers) {
        super();
        this.message = message;
        this.code = code;
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MultiMap getHeaders() {
        return headers;
    }

    public void setHeaders(MultiMap headers) {
        this.headers = headers;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
