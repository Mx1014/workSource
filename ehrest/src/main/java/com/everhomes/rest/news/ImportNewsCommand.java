package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属类型ID</li>
 * </ul>
 */
public class ImportNewsCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

}