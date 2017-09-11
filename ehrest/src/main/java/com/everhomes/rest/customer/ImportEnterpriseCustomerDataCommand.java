package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ImportEnterpriseCustomerDataCommand {

    private String ownerType;

    private Long ownerId;

    private Integer namespaceId;

    private Long communityId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
