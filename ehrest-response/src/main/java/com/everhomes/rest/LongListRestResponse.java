package com.everhomes.rest;

import java.util.List;

public class LongListRestResponse {
    private List<Long> response;

    public LongListRestResponse() {
    }

    public List<Long> getResponse() {
        return response;
    }

    public void setResponse(List<Long> response) {
        this.response = response;
    }
}
