package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class SyncLocationCommand {
    @NotNull
    private long longtitude;

    public long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(long longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
