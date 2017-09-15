package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

public class ListUniongroupMemberDetailsCommand {

    private Long groupId;

    private String ownerType;

    private Long ownerId;

    public ListUniongroupMemberDetailsCommand() {

    }

    public ListUniongroupMemberDetailsCommand(Long groupId) {
        super();
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
