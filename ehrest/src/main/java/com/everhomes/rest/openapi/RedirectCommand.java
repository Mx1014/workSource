package com.everhomes.rest.openapi;

import com.everhomes.util.StringHelper;

/**
 * Created by xq.tian on 2018/4/16.
 */
public class RedirectCommand {

    private String url;
    private String handler;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
