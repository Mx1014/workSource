package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class ListPostedTopicByOwnerIdCommand {
    private Long ownerUid;

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
