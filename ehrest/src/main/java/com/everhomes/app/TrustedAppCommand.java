package com.everhomes.app;

import javax.validation.constraints.NotNull;

public class TrustedAppCommand {
    @NotNull
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
