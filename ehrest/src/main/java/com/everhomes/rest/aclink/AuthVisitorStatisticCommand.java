package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class AuthVisitorStatisticCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;
    
    private Long start;
    
    private Long end;
    
    private String startStr;
    private String endStr;
    
    public Long getStart() {
        return start;
    }
    public void setStart(Long start) {
        this.start = start;
    }
    public Long getEnd() {
        return end;
    }
    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public String getStartStr() {
        return startStr;
    }
    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }
    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
