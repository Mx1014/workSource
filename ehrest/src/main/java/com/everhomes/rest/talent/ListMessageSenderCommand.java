// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType: 所属者类型</li>
 * <li>ownerId: 所属者id</li>
 * <li>organizationId: 组织id</li>
 * </ul>
 */
public class ListMessageSenderCommand {
	@NotNull
	private Long organizationId;
	@NotNull
	@Size(min=1)
	private String ownerType;
	@NotNull
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
