package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>ownerType：装修说明类型  参考{@link com.everhomes.rest.decoration.IllustrationType}</li>
 * <li>ownerId：审批id</li>
 * <li>communityId</li>
 * </ul>
 */
public class GetIlluStrationCommand {
    private Long communityId;
    private String ownerType;
    private Long ownerId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
}
