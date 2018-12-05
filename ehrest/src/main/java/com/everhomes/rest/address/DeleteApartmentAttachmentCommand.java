package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

/**
 *
 * <ul>
 *      <li>id: 附件id</li>
 *      <li>addressId: 门牌id</li>
 *      <li>organizationId: 公司id</li>
 *      <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 *      <li>ownerId: ownerId, communityId</li>
 *      <li>namespaceId: 域空间id，用于权限校验</li>
 *      <li>communityId: 项目编号，用于权限校验</li>
 *  </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class DeleteApartmentAttachmentCommand {
    @NotNull
    private Long id;
    @NotNull private Long addressId;
    @NotNull private Long organizationId;
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;
	private Long communityId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
