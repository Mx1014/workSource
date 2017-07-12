// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 快递相关的ownerType，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: ownerId</li>
 * <li>userId: 用户id</li>
 * </ul>
 */
public class ExpressOwner {
	private Integer namespaceId;
	private ExpressOwnerType ownerType;
	private Long ownerId;
	private Long userId;

	public ExpressOwner() {
		super();
	}

	public ExpressOwner(Integer namespaceId, ExpressOwnerType ownerType, Long ownerId, Long userId) {
		super();
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
