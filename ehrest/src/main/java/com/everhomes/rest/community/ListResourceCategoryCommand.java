// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型</li>
 * <li>ownerId:  所属实体id</li>
 * </ul>
 */
public class ListResourceCategoryCommand {

    private String ownerType;

    private Long ownerId;

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
}
