package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class DoorLinglingAuthStoreyInfo {
    private Long storey;
    private String displayName;
    public Long getStorey() {
        return storey;
    }
    public void setStorey(Long storey) {
        this.storey = storey;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
