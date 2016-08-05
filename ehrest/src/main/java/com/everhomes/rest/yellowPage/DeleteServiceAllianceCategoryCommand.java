package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> categoryId: 类型id</li>
 *  <li> ownerType: 拥有者类型：现在是community</li>
 *  <li> ownerId: 拥有者ID</li>
 * </ul>
 */
public class DeleteServiceAllianceCategoryCommand {
	
	private Long categoryId;

	private String ownerType;
	
	private Long ownerId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
