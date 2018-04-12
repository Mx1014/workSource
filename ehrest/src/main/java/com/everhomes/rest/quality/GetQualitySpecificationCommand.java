package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>specificationId: 规范id</li>
 *  <li>ownerId: 规范所属组织等的id</li>
 *  <li>ownerType: 规范所属组织类型，如enterprise</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class GetQualitySpecificationCommand {

	@NotNull
	private Long specificationId;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Integer namespaceId;

	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
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
