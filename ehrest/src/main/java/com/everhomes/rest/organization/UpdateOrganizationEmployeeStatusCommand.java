package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>employeeStatus：员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * </ul>
 */
public class UpdateOrganizationEmployeeStatusCommand {

    private Long memberId;

    private Byte employeeStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public UpdateOrganizationEmployeeStatusCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
