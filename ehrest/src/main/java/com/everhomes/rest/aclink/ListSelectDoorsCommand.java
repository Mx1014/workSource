// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * </ul>
 */
public class ListSelectDoorsCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
