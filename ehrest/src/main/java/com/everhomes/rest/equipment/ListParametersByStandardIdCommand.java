package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准所属组织等的id</li>
 *  <li>ownerType: 标准所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>standardId: 标准id</li>
 * </ul>
 */
public class ListParametersByStandardIdCommand {
	
	private Long standardId;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
