package com.everhomes.aclink;

import java.sql.Timestamp;

public class CreateDoorAuthCommand {
    private Long     userId;
    private Long     doorId;
    private Byte     authType;    
    private Long     validFromMs;
    private Long     validEndMs;
    
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
    public Byte getAuthType() {
        return authType;
    }
    public void setAuthType(Byte authType) {
        this.authType = authType;
    }
    public Long getValidFromMs() {
        return validFromMs;
    }
    public void setValidFromMs(Long validFromMs) {
        this.validFromMs = validFromMs;
    }
    public Long getValidEndMs() {
        return validEndMs;
    }
    public void setValidEndMs(Long validEndMs) {
        this.validEndMs = validEndMs;
    }
}
