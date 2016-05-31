package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class GetVisitorResponse {
    String userName;
    String doorName;
    Long createTime;
    String qr;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDoorName() {
        return doorName;
    }
    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }
    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    
    
    public String getQr() {
        return qr;
    }
    public void setQr(String qr) {
        this.qr = qr;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
