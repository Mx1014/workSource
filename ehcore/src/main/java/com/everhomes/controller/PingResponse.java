package com.everhomes.controller;

public class PingResponse {
    String response;
    
    public PingResponse(String responseText) {
        response = responseText;
    }
    
    public String getResponse() {
        return response;
    }
}
