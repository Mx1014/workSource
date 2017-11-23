// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: organizationId</li>
 * </ul>
 */
public class ListSalaryGroupCommand {

    private String ownerType;

    private Long ownerId;

    public ListSalaryGroupCommand() {

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
