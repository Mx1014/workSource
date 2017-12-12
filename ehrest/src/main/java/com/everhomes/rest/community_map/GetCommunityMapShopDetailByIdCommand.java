package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/25.
 */
public class GetCommunityMapShopDetailByIdCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long shopId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
