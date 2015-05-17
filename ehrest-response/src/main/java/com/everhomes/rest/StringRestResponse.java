package com.everhomes.rest;

public class StringRestResponse extends RestResponseBase {
    private String response;

    public StringRestResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
