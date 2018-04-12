package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerId: 所属的主体id</li>
 *  <li>ownerType: 所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ListRelatedOrgGroupsCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Integer namespaceId;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
