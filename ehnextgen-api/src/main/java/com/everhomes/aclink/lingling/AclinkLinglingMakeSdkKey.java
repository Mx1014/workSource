package com.everhomes.aclink.lingling;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.StringHelper;

public class AclinkLinglingMakeSdkKey {
    List<Long> deviceIds;
    Long keyEffecDay;

    public List<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }
    
    public Long getKeyEffecDay() {
        return keyEffecDay;
    }

    public void setKeyEffecDay(Long keyEffecDay) {
        this.keyEffecDay = keyEffecDay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
