package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryPeriodGroupId: 某期薪酬批次id</li>
 * <li>salaryGroupName: 某期薪酬批次名称</li>
 * <li>salaryPeriodYear: 某期薪酬批次年份</li>
 * <li>salaryPeriodMonth: 某期薪酬批次月份</li>
 * </ul>
 */
public class ExportPeriodSalaryEmployeesCommand {

    private String ownerType;

    private Long ownerId;

    private Long salaryPeriodGroupId;

    private String salaryGroupName;

    private String salaryPeriodYear;

    private String salaryPeriodMonth;

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

    public String getSalaryGroupName() {
        return salaryGroupName;
    }

    public void setSalaryGroupName(String salaryGroupName) {
        this.salaryGroupName = salaryGroupName;
    }

    public String getSalaryPeriodYear() {
        return salaryPeriodYear;
    }

    public void setSalaryPeriodYear(String salaryPeriodYear) {
        this.salaryPeriodYear = salaryPeriodYear;
    }

    public String getSalaryPeriodMonth() {
        return salaryPeriodMonth;
    }

    public void setSalaryPeriodMonth(String salaryPeriodMonth) {
        this.salaryPeriodMonth = salaryPeriodMonth;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
