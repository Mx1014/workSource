// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType: 快递相关的ownerType，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: ownerId</li>
 * </ul>
 */
public class ExpressOwner {
	private Integer namespaceId;
	private ExpressOwnerType ownerType;
	private Long ownerId;

	public ExpressOwner() {
		super();
	}

	public ExpressOwner(Integer namespaceId, ExpressOwnerType ownerType, Long ownerId) {
		super();
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public ExpressOwnerType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(ExpressOwnerType ownerType) {
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
