// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * <ul>
 * <li>ownerId: 范围id</li>
 * <li>ownerType: 范围类型</li>
 * </ul>
 */
public class GetTreeProjectCategoriesCommand {

    private Long ownerId;
    private String ownerType;

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
