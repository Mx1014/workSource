package com.everhomes.rest.openapi.ruian;

import com.everhomes.util.StringHelper;
import javax.validation.constraints.NotNull;

public class MallcooAutoLoginCommand {

    private String communityId;
    private String callbackUrl;

    public MallcooAutoLoginCommand() {
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
