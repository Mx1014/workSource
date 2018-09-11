// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 1-园区，4-公司。参考{@link com.everhomes.rest.common.OwnerType}</li>
 *     <li>ownerId: ownerId</li>
 * </ul>
 */
public class FindLaunchPadConfigCommand {

    private Byte ownerType;
    private Long ownerId;

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
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
