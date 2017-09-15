package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>salaryGroupId: 批次（薪酬组）id</li>
 * <li>ownerType: 'organization'</li>
 * <li>owernId: organizationId</li>
 * </ul>
 */
public class GetSalaryGroupCommand {

    private Long salaryGroupId;

    private String ownerType;

    private Long ownerId;

    public GetSalaryGroupCommand() {

    }

    public GetSalaryGroupCommand(Long salaryGroupId) {
        super();
        this.salaryGroupId = salaryGroupId;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
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
