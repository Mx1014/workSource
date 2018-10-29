package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

public class OPPushUrlInstanceConfig {
    private String url ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
