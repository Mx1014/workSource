package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>organizationId: 总公司id</li>
 * </ul>
 */
public class FileSalaryGroupCommand {

    private String ownerType;

    private Long ownerId;

    private Long organizationId;
    private String month ;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
