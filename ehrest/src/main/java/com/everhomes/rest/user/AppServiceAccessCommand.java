package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AppServiceAccessCommand {
    @NotNull
    private String appKey;
    
    @NotNull
    private String uri;
    
    @NotNull
    private String type;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
