package com.everhomes.rest.aclink;

public class AclinkMessageTestCommand {
    Long uid;
    Long doorId;
    Byte doorType;
    
    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
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
}
