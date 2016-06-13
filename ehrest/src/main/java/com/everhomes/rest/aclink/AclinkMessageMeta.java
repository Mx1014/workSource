package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkMessageMeta {
    private Long userId;
    private Long doorId;
    
    public AclinkMessageMeta() {
        
    }
    
    public AclinkMessageMeta(Long uid, Long did) {
        this.userId = uid;
        this.doorId = did;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
