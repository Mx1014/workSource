package com.everhomes.rest.openapi.ruian;

import com.everhomes.util.StringHelper;
import javax.validation.constraints.NotNull;

public class MallcooAutoLoginCommand {
    @NotNull
    private String communityId;

    public MallcooAutoLoginCommand() {
    }
    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String content) {
        this.communityId = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
