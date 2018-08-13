package com.everhomes.rest.app;

import javax.validation.constraints.NotNull;

public class TrustedAppCommand {

    @NotNull
    private String thirdPartyAppKey;

    public String getThirdPartyAppKey() {
        return thirdPartyAppKey;
    }

    public void setThirdPartyAppKey(String thirdPartyAppKey) {
        this.thirdPartyAppKey = thirdPartyAppKey;
    }
}