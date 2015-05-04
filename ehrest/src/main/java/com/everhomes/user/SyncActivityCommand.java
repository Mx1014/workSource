package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class SyncActivityCommand {
    @NotNull
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
