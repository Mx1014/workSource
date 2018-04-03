package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>ownerType: 所属主体类型</li>
 *     <li>ownerId: 所属主体id</li>
 * </ul>
 * Created by ying.xiong on 2017/10/26.
 */
public class ListDefaultChargingItemsCommand {
    private Integer namespaceId;
    private Long communityId;
    private String ownerType;
    private Long ownerId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
}
