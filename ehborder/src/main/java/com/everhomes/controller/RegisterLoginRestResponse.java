package com.everhomes.controller;

import com.everhomes.rest.RestResponseBase;

public class RegisterLoginRestResponse extends RestResponseBase {
    private String response;
    
    public RegisterLoginRestResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
