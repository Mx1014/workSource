package com.everhomes.client;

import com.everhomes.rest.RestResponseBase;

public class StringResultRestResponse extends RestResponseBase {
    private String response;
    
    public StringResultRestResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
