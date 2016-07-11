package com.everhomes.aclink.lingling;

import java.util.List;

import com.everhomes.util.StringHelper;

public class AclinkLinglingQrCodeRequest {
    private String lingLingId;
    private Long autoFloorId;
    private Long userType;
    private List<Long> floorIds;
    private List<String> sdkKeys;
    private String startTime;
    private Long endTime;
    private Long effecNumber;
    private String strKey;
    
    public String getLingLingId() {
        return lingLingId;
    }
    public void setLingLingId(String lingLingId) {
        this.lingLingId = lingLingId;
    }
    public Long getAutoFloorId() {
        return autoFloorId;
    }
    public void setAutoFloorId(Long autoFloorId) {
        this.autoFloorId = autoFloorId;
    }
    public Long getUserType() {
        return userType;
    }
    public void setUserType(Long userType) {
        this.userType = userType;
    }
    public List<Long> getFloorIds() {
        return floorIds;
    }
    public void setFloorIds(List<Long> floorIds) {
        this.floorIds = floorIds;
    }
    public List<String> getSdkKeys() {
        return sdkKeys;
    }
    public void setSdkKeys(List<String> sdkKeys) {
        this.sdkKeys = sdkKeys;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    public Long getEffecNumber() {
        return effecNumber;
    }
    public void setEffecNumber(Long effecNumber) {
        this.effecNumber = effecNumber;
    }
    public String getStrKey() {
        return strKey;
    }
    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
