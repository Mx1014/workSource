package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkMessageMeta {
    private Long userId;
    private Long doorId;
    private Byte doorType;
    
    public AclinkMessageMeta() {
        
    }
    
    public AclinkMessageMeta(Long uid, Long did, Byte doorType) {
        this.userId = uid;
        this.doorId = did;
        this.doorType = doorType;
    }
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    
    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
