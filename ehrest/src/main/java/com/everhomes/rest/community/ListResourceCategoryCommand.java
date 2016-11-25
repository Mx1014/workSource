// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型</li>
 * <li>ownerId:  所属实体id</li>
 * <li>parentId:  父id</li>
 * </ul>
 */
public class ListResourceCategoryCommand {

    private String ownerType;

    private Long ownerId;
    
    private Long parentId;

    public ListResourceCategoryCommand() {
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
