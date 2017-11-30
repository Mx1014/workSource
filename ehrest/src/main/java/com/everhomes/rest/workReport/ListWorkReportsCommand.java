package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

public class ListWorkReportsCommand {

    private Long ownerId;

    private String ownerType;

    public ListWorkReportsCommand() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
