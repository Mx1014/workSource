// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

public class InitSensitiveWordTrieCommand {

    private String url;

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
