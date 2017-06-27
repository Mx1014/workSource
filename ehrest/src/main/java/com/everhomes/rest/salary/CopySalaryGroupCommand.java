package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>gitsalaryGroupId: 薪酬批次id</li>
 * <li>salaryGroupName: 薪酬组名称</li>
 * </ul>
 */
public class CopySalaryGroupCommand {

    private Long salaryGroupId;

    private String salaryGroupName;

    public CopySalaryGroupCommand() {

    }

    public CopySalaryGroupCommand(Long salaryGroupId) {
        super();
        this.salaryGroupId = salaryGroupId;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public String getSalaryGroupName() {
        return salaryGroupName;
    }

    public void setSalaryGroupName(String salaryGroupName) {
        this.salaryGroupName = salaryGroupName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
