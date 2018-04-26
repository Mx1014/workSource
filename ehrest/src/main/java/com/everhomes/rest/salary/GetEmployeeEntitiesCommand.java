package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>organizationId: 总公司id</li>
 * <li>detailId: 用户detailId</li>
 * </ul>
 */
public class GetEmployeeEntitiesCommand {

    private String ownerType;

    private Long ownerId;

    private Long organizationId;

    private Long detailId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
}
