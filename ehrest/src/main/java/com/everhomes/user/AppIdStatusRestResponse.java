package com.everhomes.user;

import com.everhomes.rest.RestResponseBase;

public class AppIdStatusRestResponse extends RestResponseBase {
    
    AppIdStatusResponse response;

    public AppIdStatusResponse getResponse() {
        return response;
    }

    public void setResponse(AppIdStatusResponse response) {
        this.response = response;
    }
    
}
