package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

/**
 *
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>addressId: 门牌id</li>
 *      <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 *      <li>ownerId: ownerId, communityId</li>
 *  </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class ListApartmentAttachmentsCommand {

    @NotNull
    private Long addressId;
    @NotNull private Long organizationId;

    private String ownerType;
    private Long ownerId;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
