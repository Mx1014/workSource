package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *      <li>addressId: 门牌id</li>
 *      <li>organizationId: 公司id</li>
 *      <li>contentUri: 附件URI</li>
 *      <li>attachmentName: 附件名称</li>
 *      <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 *      <li>ownerId: ownerId, communityId</li>
 *  </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class UploadApartmentAttachmentCommand {
    @NotNull
    private Long addressId;
    @NotNull
    private Long organizationId;
    @NotNull private String contentUri;
    @NotNull private String attachmentName;
    
    private Integer namespaceId;
	private Long communityId;

    private String ownerType;
    private Long ownerId;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
