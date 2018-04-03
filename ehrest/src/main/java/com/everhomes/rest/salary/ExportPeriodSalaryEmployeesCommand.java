package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryPeriodGroupId: 某期薪酬批次id</li>
 * </ul>
 */
public class ExportPeriodSalaryEmployeesCommand {

    private String ownerType;

    private Long ownerId;

    private Long salaryPeriodGroupId;

    public ExportPeriodSalaryEmployeesCommand() {
    }

    public Long getSalaryPeriodGroupId() {
        return salaryPeriodGroupId;
    }

    public void setSalaryPeriodGroupId(Long salaryPeriodGroupId) {
        this.salaryPeriodGroupId = salaryPeriodGroupId;
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
