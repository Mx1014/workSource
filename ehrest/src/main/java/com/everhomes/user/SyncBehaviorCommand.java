package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class SyncBehaviorCommand {
    @NotNull
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
