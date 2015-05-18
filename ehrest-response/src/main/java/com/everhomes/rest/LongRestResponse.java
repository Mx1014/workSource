package com.everhomes.rest;

public class LongRestResponse extends RestResponseBase {
    private Long response;

    public LongRestResponse() {
    }

    public Long getResponse() {
        return response;
    }

    public void setResponse(Long response) {
        this.response = response;
    }
}
