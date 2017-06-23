package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>salaryGroupId: 批次（薪酬组）id</li>
 * </ul>
 */
public class GetSalaryGroupCommand {

    private Long salaryGroupId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
