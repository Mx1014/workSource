package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AclinkUpdateLinglingStoreyCommand {
    @NotNull
    private Long authId;
    
    @NotNull
    private Long newStorey;
    
    public Long getAuthId() {
        return authId;
    }
    public void setAuthId(Long authId) {
        this.authId = authId;
    }
    public Long getNewStorey() {
        return newStorey;
    }
    public void setNewStorey(Long newStorey) {
        this.newStorey = newStorey;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
