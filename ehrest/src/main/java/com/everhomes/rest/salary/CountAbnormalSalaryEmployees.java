package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: organizationId</li>
 * <li>ownerType: organization</li>
 * </ul>
 */
public class CountAbnormalSalaryEmployees {

    private Long ownerId;

    private String ownerType;

    public CountAbnormalSalaryEmployees() {
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
